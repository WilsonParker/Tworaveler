package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.Comment;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.Page.FragmentFeedSchedule;
import com.developer.hare.tworaveler.Listener.OnListScrollListener;
import com.developer.hare.tworaveler.Model.LikeModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hare on 2017-08-01.
 */

public class FeedCityListAdapter extends RecyclerView.Adapter<FeedCityListAdapter.ViewHolder> {
    private ArrayList<ScheduleModel> items;
    private OnListScrollListener onListScrollListenrer;
    private int scrollCount = 0;
    private ImageManager imageManager = ImageManager.getInstance();

    public FeedCityListAdapter(ArrayList<ScheduleModel> items, OnListScrollListener onListScrollListenrer) {
        this.items = items;
        this.onListScrollListenrer = onListScrollListenrer;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false), parent.getContext());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private ImageView IV_cover,IV_like;
        private CircleImageView CV_profile;
        private TextView TV_nickname, TV_message, TV_title, TV_date, TV_like, TV_comment;
        private LinearLayout LL_like, LL_comment;
        private ScheduleModel model;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            this.context = context;
            IV_cover = uiFactory.createView(R.id.item_feed$IV_cover);
            CV_profile = uiFactory.createView(R.id.item_feed$CV_profile);
            TV_date = uiFactory.createView(R.id.item_feed$TV_date);
            TV_nickname = uiFactory.createView(R.id.item_feed$TV_nickname);
            TV_message = uiFactory.createView(R.id.item_feed$TV_message);
            TV_title = uiFactory.createView(R.id.item_feed$TV_title);
            TV_date = uiFactory.createView(R.id.item_feed$TV_date);
            TV_like = uiFactory.createView(R.id.item_feed$TV_like);
            TV_comment = uiFactory.createView(R.id.item_feed$TV_comment);
            LL_like = uiFactory.createView(R.id.item_feed$LL_like);
            LL_comment = uiFactory.createView(R.id.item_feed$LL_comment);
            IV_like = uiFactory.createView(R.id.item_feed$IV_like);

            LL_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Comment.class);
                    intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, model);
                    context.startActivity(intent);
                }
            });

            ArrayList<TextView> textlist1 = new ArrayList<>();
            ArrayList<TextView> textlist2 = new ArrayList<>();
            textlist1.add(TV_nickname);
            textlist1.add(TV_date);
            textlist1.add(TV_like);
            textlist1.add(TV_comment);
            FontManager.getInstance().setFont(textlist1, "Roboto-Medium.ttf");
            textlist2.add(TV_message);
            textlist2.add(TV_title);
            FontManager.getInstance().setFont(textlist2, "NotoSansCJKkr-Medium.otf");
        }

        public void toBind(ScheduleModel model) {
            this.model = model;
            IV_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager.getInstance().setFragmentContent(FragmentFeedSchedule.newInstance(model));
                }
            });
            ImageManager imageManager = ImageManager.getInstance();
            imageManager.loadImage(imageManager.createRequestCreator(context, model.getTrip_pic_url(), ImageManager.FIT_TYPE).centerCrop(), IV_cover);
            imageManager.loadImage(imageManager.createRequestCreator(context, model.getProfile_pic_thumbnail(), ImageManager.FIT_TYPE).placeholder(R.drawable.image_history_profile).centerCrop(), CV_profile);
            TV_nickname.setText(model.getNickname()+"");
            TV_message.setText(model.getStatus_message()+"");
            TV_title.setText(model.getTripName()+"");
            TV_like.setText(model.getLikeCount()+"");
            TV_comment.setText(model.getCommentCount()+"");
            TV_date.setText(model.getStart_date() + " ~ " + model.getEnd_date());

            LL_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeClick(model.isLike());
                }
            });
            changeLike(model.isLike());
        }

        private void changeLike(boolean isLike){
            if(isLike){
                imageManager.loadImage(context, R.drawable.icon_heart_click, IV_like, ImageManager.FIT_TYPE);
            }else{
                imageManager.loadImage(context, R.drawable.icon_heart_unclick, IV_like, ImageManager.FIT_TYPE);
//                imageManager.loadImage(imageManager.createRequestCreator(context, R.drawable.icon_heart_unclick, ImageManager.FIT_TYPE) .centerCrop(), IV_like);
            }
        }
        private void likeClick(boolean isLike){
            if(isLike){
                Net.getInstance().getFactoryIm().modifyUnLike(SessionManager.getInstance().getUserModel().getUser_no(), model.getTrip_no()).enqueue(new Callback<ResponseModel<LikeModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<LikeModel>> call, Response<ResponseModel<LikeModel>> response) {
                        Log_HR.log(Log_HR.LOG_INFO,HomeListAdapter.class, "onResponse","body : "+response.body().getSuccess());
                        Log_HR.log(Log_HR.LOG_INFO,HomeListAdapter.class, "onResponse","body : "+response.body().getMessage());
                        Log_HR.log(Log_HR.LOG_INFO,HomeListAdapter.class, "onResponse","body : "+response.body().getResult().toString());

                        if(response.isSuccessful()){
                            switch (response.body().getSuccess()){
                                case DataDefinition.Network.CODE_SUCCESS:
                                    changeLike(false);
                                    int likeCount =model.getLikeCount()-1;
                                    TV_like.setText(""+likeCount );
                                    model.setLikeCount(likeCount);
                                    break;

                            }
                        }else{
                            Log_HR.log(Log_HR.LOG_INFO,HomeListAdapter.class, "onResponse","onResponse is not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<LikeModel>> call, Throwable t) {
                        Log_HR.log(HomeListAdapter.class, "onFailure", t);
                    }
                });
            }else {
                Net.getInstance().getFactoryIm().modifyLike(SessionManager.getInstance().getUserModel().getUser_no(), model.getTrip_no()).enqueue(new Callback<ResponseModel<LikeModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<LikeModel>> call, Response<ResponseModel<LikeModel>> response) {
                        if(response.isSuccessful()){
                            switch (response.body().getSuccess()){
                                case DataDefinition.Network.CODE_SUCCESS:
                                    changeLike(true);
                                    int likeCount =model.getLikeCount()+1;
                                    TV_like.setText(""+likeCount);
                                    model.setLikeCount(likeCount);
                                    break;
                            }
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<LikeModel>> call, Throwable t) {

                    }
                });
            }
            model.setLike(!model.isLike());
        }
    }

}
