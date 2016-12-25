//package com.eternallove.demo.zuccfairy.ui.adapters;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.support.v7.widget.PopupMenu;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.Transformation;
//import com.bumptech.glide.load.engine.Resource;
//import com.bumptech.glide.load.resource.SimpleResource;
//import com.eternallove.demo.zuccfairy.R;
//import com.eternallove.demo.zuccfairy.modle.ReceivedBean;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
//
///**
// * @description:
// * @author: eternallove
// * @date: 2016/11/7
// */
//public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ReceivedHolder> {
//
//    private static final int TYPE_MESSAGE_ME = 0x0;
//    private static final int TYPE_PICTURE_OTHER = 0x1;
//
//    private List<ReceivedBean> mReceivedList;
//
//    private Context mContext;
//
//    public ChatAdapter(Context context,List<ReceivedBean> receivedList) {
//        this.mReceivedList = receivedList;
//        this.mContext = context;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return TYPE_MESSAGE_ME;
//    }
//
//    @Override
//    public ReceivedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType){
//            case TYPE_MESSAGE_ME:
//                return new MessageHolder(
//                        LayoutInflater.from(mContext).inflate(R.layout.row_received_me_message, parent, false));
//                break;
//            case TYPE_PICTURE_OTHER:
//                return new GeneralViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_general, parent, false));
//                break;
//            default:return null;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(ReceivedHolder holder, int position) {
//        if (getItemViewType(position) == TYPE_HEAD) {
//            MeMessageHolder meMessageHolder = (MeMessageHolder) holder;
//            if(mHeadBean.getHeadAvatar() == null){
//                meMessageHolder.avatar.setImageResource(ICON_LIST[0]);
//            } else {
//                Glide.with(mContext)
//                        .load(mHeadBean.getHeadAvatar())
//                        .placeholder(R.color.colorImagePlaceHolder)
//                        .into(meMessageHolder.avatar);
//            }
//            if(mHeadBean.getHeadBackground() == null){
//                meMessageHolder.background.setImageResource(R.drawable.ic_background);
//            } else {
//                Glide.with(mContext)
//                        .load(mHeadBean.getHeadBackground())
//                        .centerCrop()
//                        .placeholder(R.color.colorImagePlaceHolder)
//                        .into(meMessageHolder.background);
//            }
//            meMessageHolder.name.setText(mHeadBean.getHeadName());
//        } else {
//            GeneralViewHolder generalViewHolder = (GeneralViewHolder) holder;
//            MomentBean momentBean = mMomentBeanList.get(position - 1);
//            if(momentBean.getAvatar() ==null){
//                generalViewHolder.avatar.setImageResource(ICON_LIST[position%10]);
//            } else {
//                Glide.with(mContext)
//                        .load(Uri.parse(momentBean.getAvatar()))
//                        .placeholder(R.color.colorImagePlaceHolder)
//                        .into(generalViewHolder.avatar);
//            }
//            //填充用户名
//            generalViewHolder.name.setText(momentBean.getName());
//            //填充用户发布信息
//            if(momentBean.getContent() != null){
//                generalViewHolder.content.setText(momentBean.getContent());
//                generalViewHolder.content.setVisibility(View.VISIBLE);
//            } else {
//                generalViewHolder.content.setVisibility(View.GONE);
//            }
//            //填充用户发布的连接信息
//            if(momentBean.getLinkData()!=null){
//                final LinkBean linkData = momentBean.getLinkData();
//                if(linkData.getLinkimg()==null){
//                    generalViewHolder.linkimg.setImageResource(ICON_LIST[(position+1)%10]);
//                    generalViewHolder.linkimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                } else {
//                    Glide.with(mContext)
//                            .load(linkData.getLinkimg())
//                            .centerCrop()
//                            .placeholder(R.color.colorImagePlaceHolder)
//                            .into(generalViewHolder.linkimg);
//                }
//                generalViewHolder.linkTitle.setText(linkData.getLinktitle());
//                generalViewHolder.linkTitle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                       Toast.makeText(mContext,"将要跳转到:"+linkData.getUrl(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//                generalViewHolder.link.setVisibility(View.VISIBLE);
//            }else{
//                generalViewHolder.link.setVisibility(View.GONE);
//            }
//            //填充用户发布图片信息
//            if (momentBean.getImageList() != null && momentBean.getImageList().size() > 0) {
//                List<String> imageList = momentBean.getImageList();
//                generalViewHolder.gridLayout.removeAllViews();
//
//                int size = imageList.size();
//                int rowCount = (int) Math.sqrt(size);
//                int columnCount = size / rowCount;
//
//                int k = 0;
//                for (int i = 0; i < rowCount; i++) {
//                    for (int j = 0; j < columnCount; j++) {
//                        if (k >= size || k >= 9) {
//                            // 超过总数
//                            break;
//                        }
//                        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
//                        layoutParams.setGravity(Gravity.CENTER);
//                        layoutParams.rowSpec = GridLayout.spec(i);
//                        layoutParams.columnSpec = GridLayout.spec(j);
//
//                        if (size == 1) {
//                            // 如果只有一张就完整显示
//                            layoutParams.width = WRAP_CONTENT;
//                            layoutParams.height = WRAP_CONTENT;
//
//                            ImageView imageView = new ImageView(mContext);
//                            imageView.setMinimumHeight((int) mContext.getResources()
//                                    .getDimension(R.dimen.item_general_image_grid_image_size));
//                            imageView.setMinimumWidth((int) mContext.getResources()
//                                    .getDimension(R.dimen.item_general_image_grid_image_size));
//
//                            imageView.setLayoutParams(layoutParams);
//                            generalViewHolder.gridLayout.addView(imageView);
//
//                            final int maxWidth = (int) mContext.getResources()
//                                    .getDimension(R.dimen.item_general_image_grid_image_max_width);
//
//                            Glide.with(mContext)
//                                    .load(imageList.get(k))
//                                    .asBitmap()
//                                    .transform(new Transformation<Bitmap>() {
//                                        @Override
//                                        public Resource<Bitmap> transform(Resource<Bitmap> resource,
//                                                                          int outWidth,
//                                                                          int outHeight) {
//                                            int height = resource.get().getHeight();
//                                            int width = resource.get().getWidth();
//                                            if (width > maxWidth) {
//                                                int time = width / maxWidth;
//                                                width /= time;
//                                                height /= time;
//                                            }
//                                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
//                                                    resource.get(), width, height, false);
//
//                                            return new SimpleResource<>(resizedBitmap);
//                                        }
//
//                                        @Override
//                                        public String getId() {
//                                            return "";
//                                        }
//                                    })
//                                    .placeholder(R.color.colorImagePlaceHolder)
//                                    .into(imageView);
//                        } else {
//                            // 否则就显示为一个方块
//                            layoutParams.width = (int) mContext.getResources()
//                                    .getDimension(R.dimen.item_general_image_grid_image_size);
//                            layoutParams.height = (int) mContext.getResources()
//                                    .getDimension(R.dimen.item_general_image_grid_image_size);
//
//                            int margin = (int) mContext.getResources()
//                                    .getDimension(R.dimen.item_general_image_grid_image_margin);
//                            layoutParams.setMargins(margin, margin, margin, margin);
//
//                            ImageView imageView = new ImageView(mContext);
//                            imageView.setLayoutParams(layoutParams);
//                            generalViewHolder.gridLayout.addView(imageView);
//
//                            Glide.with(mContext)
//                                    .load(imageList.get(k))
//                                    .placeholder(R.color.colorImagePlaceHolder)
//                                    .centerCrop()
//                                    .into(imageView);
//                        }
//                        k++;
//                    }
//                }
//                generalViewHolder.gridLayout.setVisibility(View.VISIBLE);
//            } else {
//                generalViewHolder.gridLayout.setVisibility(View.GONE);
//            }
//            //填充评论点赞数据
//            boolean isShowComentOrLike = false;
//            generalViewHolder.like.removeAllViews();
//            generalViewHolder.comment.removeAllViews();
//            if(momentBean.getLikeData()!= null) {
//                generalViewHolder.like.setFillMode(AutoWrapLineLayout.MODE_WRAP_CONTENT);
//                for(LikeBean item : momentBean.getLikeData()) {
//                    View likeItem = LayoutInflater.from(mContext).inflate(R.layout.item_general_like, null);
//                    TextView userName = (TextView) likeItem.findViewById(R.id.textView_item_general_like_username);
//                    userName.setText(item.getUsername());
//                    generalViewHolder.like.addView(likeItem);
//                }
//                isShowComentOrLike = true;
//            }
//            if(momentBean.getCommentData()!=null){
//                for(CommentBean item : momentBean.getCommentData()){
//                    View commentItem = LayoutInflater.from(mContext).inflate(R.layout.item_general_comment, null);
//                    TextView initiatorName = (TextView) commentItem.findViewById(R.id.textView_item_general_comment_initiatorName);
//                    TextView reply = (TextView) commentItem.findViewById(R.id.textView_item_general_comment_1);
//                    TextView recipientName = (TextView) commentItem.findViewById(R.id.textView_item_general_comment_recipientName);
//                    TextView commentContent = (TextView) commentItem.findViewById(R.id.textView_item_general_comment_content);
//                    if(item.getRecipient_name()!=null){
//                        recipientName.setText(item.getRecipient_name());
//                    }
//                    else{
//                        reply.setVisibility(View.GONE);
//                        recipientName.setVisibility(View.GONE);
//                    }
//                    commentContent.setText(item.getContent());
//                    initiatorName.setText(item.getInitiator_name());
//                    generalViewHolder.comment.addView(commentItem);
//                }
//                isShowComentOrLike = true;
//            }
//            if(isShowComentOrLike){
//                generalViewHolder.likeOrComment.setVisibility(View.VISIBLE);
//            }else{
//                generalViewHolder.likeOrComment.setVisibility(View.GONE);
//            }
//            generalViewHolder.date
//                    .setText(DateUtil.getDateString(mContext, momentBean.getPublishDate()));
//            generalViewHolder.commentButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    PopupMenu popupMenu = new PopupMenu(mContext,view);
//                    popupMenu.getMenuInflater()
//                            .inflate(R.menu.menu_bot_dir_1,popupMenu.getMenu());
//                    popupMenu.setGravity(Gravity.LEFT);
//                    popupMenu.show();
//                }
//            });
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mReceivedList.size() + 1;
//    }
//
//    static class ReceivedHolder extends RecyclerView.ViewHolder {
//
//        ReceivedHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this,itemView);
//        }
//    }
//
//    static class MessageHolder extends ReceivedHolder {
//        @BindView(R.id.tv_chatcontent)  TextView tvChatContent;
//        @BindView(R.id.timestamp)       TextView timestamp;
//        @BindView(R.id.tv_userid)       TextView tvUserid;
//        @BindView(R.id.img_userhead)    ImageView imgUserhead;
//
//        MessageHolder(View itemView) {
//            super(itemView);
//        }
//    }
//    static class PicturHolder extends ReceivedHolder {
////TODO
////        @BindView(R.id.tv_chatcontent)  TextView tvChatContent;
////        @BindView(R.id.timestamp)       TextView timestamp;
////        @BindView(R.id.tv_userid)       TextView tvUserid;
////        @BindView(R.id.img_userhead)    ImageView imgUserhead;
//        PicturHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//}
