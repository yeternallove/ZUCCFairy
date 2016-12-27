package com.eternallove.demo.zuccfairy.ui.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eternallove.demo.zuccfairy.R;
import com.eternallove.demo.zuccfairy.modle.ReceivedBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/25
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ReceivedHolder> {

    private static final int TYPE_BOTTOM = 0x0;
    private static final int TYPE_MESSAGE_SEND = 0x1;
    private static final int TYPE_PICTURE_SEND = 0x2;
    private static final int TYPE_MESSAGE_RECEIVED = 0x3;
    private static final int TYPE_PICTURE_RECEIVED = 0x4;
    private final String mID;
    private List<ReceivedBean> mPastList;
    private List<ReceivedBean> mNewList;

    private Context mContext;
    private LayoutInflater layoutInflater;

    public ChatAdapter(Context context, List<ReceivedBean> pastList, List<ReceivedBean> newList) {
        this.mPastList = pastList;
        this.mNewList = newList;
        this.mContext = context;
        this.mID = PreferenceManager.getDefaultSharedPreferences(context).getString("user_id", null);
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BOTTOM;
        } else {
            if (getRecivedBean(position).getUser_id() == mID) {
                return TYPE_MESSAGE_SEND;
            } else {
                return TYPE_MESSAGE_RECEIVED;
            }
        }
    }

    @Override
    public ReceivedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_MESSAGE_SEND:
                return new MessageHolder(layoutInflater.inflate(R.layout.item_sent_message, parent, false));
            case TYPE_MESSAGE_RECEIVED:
                return new MessageHolder(layoutInflater.inflate(R.layout.item_received_message, parent, false));
            case TYPE_PICTURE_SEND:
                return new PictureHolder(layoutInflater.inflate(R.layout.item_sent_picture, parent, false));
            case TYPE_PICTURE_RECEIVED:
                return new PictureHolder(layoutInflater.inflate(R.layout.item_received_picture, parent, false));
            case TYPE_BOTTOM:
                return new ReceivedHolder(layoutInflater.inflate(R.layout.item_chat_null, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ReceivedHolder holder, int position) {

        switch (getItemViewType(position)) {
            case TYPE_MESSAGE_SEND:
                onBindViewMessageHolder(holder, getRecivedBean(position));
                break;
            case TYPE_MESSAGE_RECEIVED:
                onBindViewMessageHolder(holder, getRecivedBean(position));
                break;
            case TYPE_PICTURE_SEND:
                onBindViewPictureHolder(holder, getRecivedBean(position));
                break;
            case TYPE_PICTURE_RECEIVED:
                onBindViewPictureHolder(holder, getRecivedBean(position));
                break;
            default:
                break;
        }
    }

    private void onBindViewMessageHolder(ReceivedHolder holder, ReceivedBean receivedBean) {
        MessageHolder messageHolder = (MessageHolder) holder;
        messageHolder.ChatContent.setText(receivedBean.getMessage());
//        messageHolder.imgUserhead
        messageHolder.Timestamp.setVisibility(View.GONE);
//        messageHolder.UserName
    }

    private void onBindViewPictureHolder(ReceivedHolder holder, ReceivedBean receivedBean) {
        PictureHolder pictureHolder = (PictureHolder) holder;
//
    }

    private ReceivedBean getRecivedBean(int position) {
        int length = mNewList.size();
        if (position > length) {
            return mPastList.get(position - length - 1);
        } else {
            return mNewList.get(length - position);
        }
    }

    @Override
    public int getItemCount() {
        return mNewList.size() + mPastList.size() + 1;
    }

    static class ReceivedHolder extends RecyclerView.ViewHolder {

        ReceivedHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class MessageHolder extends ReceivedHolder {
        @BindView(R.id.tv_chatcontent)
        TextView ChatContent;
        @BindView(R.id.timestamp)
        TextView Timestamp;
        @BindView(R.id.tv_userName)
        TextView UserName;
        @BindView(R.id.img_userhead)
        ImageView Userhead;

        MessageHolder(View itemView) {
            super(itemView);
        }
    }

    static class PictureHolder extends ReceivedHolder {
        @BindView(R.id.percentage)
        TextView Percentage;
        @BindView(R.id.timestamp)
        TextView Timestamp;
        @BindView(R.id.tv_userName)
        TextView UserName;
        @BindView(R.id.img_userhead)
        ImageView Userhead;

        PictureHolder(View itemView) {
            super(itemView);
        }
    }

}
