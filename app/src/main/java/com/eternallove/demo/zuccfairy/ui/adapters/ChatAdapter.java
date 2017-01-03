package com.eternallove.demo.zuccfairy.ui.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.db.FairyDB;
import com.eternallove.demo.zuccfairy.modle.ChatMessageBean;
import com.eternallove.demo.zuccfairy.ui.activities.CardAcitvity;
import com.eternallove.demo.zuccfairy.util.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/25
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private static final int TYPE_BOTTOM = 0x0;
    private static final int TYPE_MESSAGE_SEND = 0x1;
    private static final int TYPE_PICTURE_SEND = 0x2;
    private static final int TYPE_MESSAGE_RECEIVED = 0x3;
    private static final int TYPE_PICTURE_RECEIVED = 0x4;
    private final String mID;
    private List<ChatMessageBean> mPastList;
    private List<ChatMessageBean> mNewList;

    private Context mContext;
    private LayoutInflater layoutInflater;
    private ClipboardManager cmb;
    private FairyDB fairyDB;

    public ChatAdapter(Context context, List<ChatMessageBean> pastList, List<ChatMessageBean> newList) {
        this.mPastList = pastList;
        this.mNewList = newList;
        this.mContext = context;
        this.mID = PreferenceManager.getDefaultSharedPreferences(context).getString("user_id",null);
        this.layoutInflater = LayoutInflater.from(mContext);
        this.cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        this.fairyDB = FairyDB.getInstance(context);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 ){
            return TYPE_BOTTOM;
        }
        else {
            ChatMessageBean chatMessageBean = getRecivedBean(position);
            if(chatMessageBean.getSender_id().equals(mID)){
                if(chatMessageBean.getMessage()==null)
                    return TYPE_PICTURE_SEND;
                return TYPE_MESSAGE_SEND;
            }else {
                if(chatMessageBean.getMessage()==null)
                    return TYPE_PICTURE_RECEIVED;
                return TYPE_MESSAGE_RECEIVED;
            }
        }
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_MESSAGE_SEND:
                return new MessageHolder(layoutInflater.inflate(R.layout.item_sent_message, parent, false));
            case TYPE_MESSAGE_RECEIVED:
                return new MessageHolder(layoutInflater.inflate(R.layout.item_chat_message, parent, false));
            case TYPE_PICTURE_SEND:
                return new PictureHolder(layoutInflater.inflate(R.layout.item_sent_picture, parent, false));
            case TYPE_PICTURE_RECEIVED:
                return new PictureHolder(layoutInflater.inflate(R.layout.item_chat_picture, parent, false));
            case TYPE_BOTTOM:
                return new ChatHolder(layoutInflater.inflate(R.layout.item_chat_null,parent, false));
            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {

        switch (getItemViewType(position)){
            case TYPE_MESSAGE_SEND:
                onBindViewMessageHolder(holder,position);
                break;
            case TYPE_MESSAGE_RECEIVED:
                onBindViewMessageHolder(holder,position);
                break;
            case TYPE_PICTURE_SEND:
                onBindViewPictureHolder(holder,position);
                break;
            case TYPE_PICTURE_RECEIVED:
                onBindViewPictureHolder(holder,position);
                break;
            default:break;
        }
    }
    private void onBindViewMessageHolder(ChatHolder holder, int position){
        ChatMessageBean chatMessageBean = getRecivedBean(position);
        MessageHolder messageHolder = (MessageHolder) holder;
        messageHolder.ChatContent.setText(chatMessageBean.getMessage());
        messageHolder.ChatContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                popupView = view;
                PopupMenu popupMenu = new PopupMenu(mContext,view);
                popupMenu.getMenuInflater()
                        .inflate(R.menu.menu_clipboard,popupMenu.getMenu());
                popupMenu.setGravity(Gravity.CENTER);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if(item.getItemId() == R.id.action_clip_copy){
                                    if(view instanceof TextView) {
                                        cmb.setPrimaryClip(ClipData.newPlainText("Message",((TextView) view).getText().toString()));
                                    }
                                    Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
                                }
                                else if(item.getItemId() == R.id.action_clip_delete){
                                    boolean delete;
                                    delete= fairyDB.deleteChat(chatMessageBean.getId());
                                    delete = removeReciveditem(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, delete?"删除成功":"删除失败", Toast.LENGTH_SHORT).show();
                                }
                                return false;
                            }
                        }
                );
                return false;
            }
        });
//        messageHolder.imgUserhead
        if(position < getItemCount()-1){
            ChatMessageBean chatMessageOld = getRecivedBean(position+1);
            if(chatMessageOld.getTimestampe()-chatMessageBean.getTimestampe() > 1800000){
                messageHolder.Timestamp.setText(DateUtil.getReportDate(chatMessageBean.getTimestampe()));
            }else{
                messageHolder.Timestamp.setVisibility(View.GONE);
            }
        }else {
            messageHolder.Timestamp.setText(DateUtil.getReportDate(chatMessageBean.getTimestampe()));
        }
        messageHolder.UserName.setVisibility(View.GONE);
    }
    private void onBindViewPictureHolder(ChatHolder holder, int position){
        ChatMessageBean chatMessageBean = getRecivedBean(position);
        PictureHolder pictureHolder = (PictureHolder) holder;
        if(position < getItemCount()-1){
            ChatMessageBean chatMessageOld = getRecivedBean(position+1);
            if(chatMessageOld.getTimestampe()-chatMessageBean.getTimestampe() > 1800000){
                pictureHolder.Timestamp.setText(DateUtil.getReportDate(chatMessageBean.getTimestampe()));
            }else{
                pictureHolder.Timestamp.setVisibility(View.GONE);
            }
        }else {
            pictureHolder.Timestamp.setText(DateUtil.getReportDate(chatMessageBean.getTimestampe()));
        }

        pictureHolder.progressBar.setVisibility(View.GONE);
        pictureHolder.Percentage.setVisibility(View.GONE);
//        pictureHolder.Userhead

        Glide.with(mContext)
            .load(Integer.valueOf(chatMessageBean.getPicture()))
            .placeholder(R.color.colorImagePlaceHolder)
            .into(pictureHolder.sendPicture);
        pictureHolder.sendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardAcitvity.actionStart(mContext,chatMessageBean.getId());
            }
        });
        pictureHolder.sendPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                popupView = view;
                PopupMenu popupMenu = new PopupMenu(mContext,view);
                popupMenu.getMenuInflater()
                        .inflate(R.menu.menu_clipboard,popupMenu.getMenu());
                popupMenu.setGravity(Gravity.CENTER);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem item) {
                         if(item.getItemId() == R.id.action_clip_copy){
                             if(view instanceof TextView) {
                                 cmb.setPrimaryClip(ClipData.newPlainText("Message",((TextView) view).getText().toString()));
                             }
                             Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
                         }
                         else if(item.getItemId() == R.id.action_clip_delete){
                             boolean delete;
                             fairyDB.deleteChat(chatMessageBean.getId());
                             delete = removeReciveditem(position);
                             notifyDataSetChanged();
                             Toast.makeText(mContext, delete?"删除成功":"删除失败", Toast.LENGTH_SHORT).show();
                         }
                         return false;
                     }
                 }
                );
                return false;
            }
        });
    }

    private ChatMessageBean getRecivedBean(int position){
        int length = mNewList.size();
        if(position > length ){
            return mPastList.get(position - length - 1);
        }else{
            return mNewList.get(length  - position);
        }
    }
    private boolean removeReciveditem(int position){
        int length = mNewList.size();
        if(position > length ){
            mPastList.remove(position - length - 1);
        }else{
           mNewList.remove(length  - position);
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return mNewList.size()+ mPastList.size() + 1;
    }

    static class ChatHolder extends RecyclerView.ViewHolder {

        ChatHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    static class MessageHolder extends ChatHolder {
        @BindView(R.id.tv_chatcontent)  TextView ChatContent;
        @BindView(R.id.timestamp)       TextView Timestamp;
        @BindView(R.id.tv_userName)     TextView UserName;
        @BindView(R.id.img_userhead)    ImageView Userhead;

        MessageHolder(View itemView) {
            super(itemView);
        }
    }
    static class PictureHolder extends ChatHolder {
        @BindView(R.id.iv_sendPicture)  ImageView sendPicture;
        @BindView(R.id.progressBar_picture)     ProgressBar progressBar;
        @BindView(R.id.percentage)      TextView Percentage;
        @BindView(R.id.timestamp)       TextView Timestamp;
        @BindView(R.id.tv_userName)       TextView UserName;
        @BindView(R.id.img_userhead)    ImageView Userhead;
        PictureHolder(View itemView) {
            super(itemView);
        }
    }

}
