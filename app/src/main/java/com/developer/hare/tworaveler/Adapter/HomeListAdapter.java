package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.Comment;
import com.developer.hare.tworaveler.Activity.MyScheduleModify;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.Page.FragmentMyPageSchedule;
import com.developer.hare.tworaveler.Listener.OnListScrollListener;
import com.developer.hare.tworaveler.Model.Request.LikeModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.ScrollEndMethod;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hare on 2017-08-01.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private ArrayList<ScheduleModel> items;
    private OnListScrollListener onListScrollListener;
    private ImageManager imageManager = ImageManager.getInstance();

    public HomeListAdapter(ArrayList<ScheduleModel> items, OnListScrollListener onListScrollListener) {
        this.items = items;
        this.onListScrollListener = onListScrollListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage, parent, false), parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
        ScrollEndMethod.getInstance().actionAfterScrollEnd(items.size(), position, onListScrollListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView TV_title, TV_date, TV_like, TV_commenet;
        private ImageView IV_cover, IV_like;
        private LinearLayout IV_btn, LL_like, LL_comment;
        private PopupMenu popupMenu;
        private ScheduleModel model;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            IV_cover = uiFactory.createView(R.id.item_mypage$IV_cover);
            TV_title = uiFactory.createView(R.id.item_mypage$TV_title);
            TV_date = uiFactory.createView(R.id.item_mypage$TV_date);
            TV_like = uiFactory.createView(R.id.item_mypage$TV_like);
            TV_commenet = uiFactory.createView(R.id.item_mypage$TV_comment);
            IV_btn = uiFactory.createView(R.id.item_mypage$IV_btn);
            IV_like = uiFactory.createView(R.id.item_mypage$IV_like);
            LL_like = uiFactory.createView(R.id.item_mypage$LL_like);
            LL_comment = uiFactory.createView(R.id.item_mypage$LL_comment);

            LL_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Comment.class);
                    intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, model);
                    context.startActivity(intent);
                }
            });
            IV_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMenu = new PopupMenu(context, view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    Menu menu = popupMenu.getMenu();

                    inflater.inflate(R.menu.popup_menu, menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;
                            switch(item.getItemId()){
                                case R.id.popup_menu$modify:
                                    intent = new Intent(context, MyScheduleModify.class);
                                    context.startActivity(intent);
                                    break;
                                case R.id.popup_menu$delete:
                                    intent = new Intent(context, MyScheduleModify.class);
                                    context.startActivity(intent);
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        public void toBind(ScheduleModel model) {
            this.model = model;
            IV_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager.getInstance().setFragmentContent(FragmentMyPageSchedule.newInstance(model));
                }
            });
            ImageManager imageManager = ImageManager.getInstance();
            imageManager.loadImage(imageManager.createRequestCreator(context, model.getTrip_pic_url(), ImageManager.FIT_TYPE).centerCrop(), IV_cover);
            TV_title.setText(model.getTripName());
            TV_date.setText(model.getStart_date() + " ~ " + model.getEnd_date());
            TV_like.setText(model.getLikeCount() + "");
            TV_commenet.setText(model.getCommentCount() + "");
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
                imageManager.loadImage(imageManager.createRequestCreator(context, R.drawable.icon_heart_unclick, ImageManager.FIT_TYPE) .centerCrop(), IV_like);
            }
        }
        private void likeClick(boolean isLike){
            if(isLike){
                Net.getInstance().getFactoryIm().modifyUnLike(new LikeModel(SessionManager.getInstance().getUserModel().getUser_no(), model.getTrip_no())).enqueue(new Callback<ResponseModel<LikeModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<LikeModel>> call, Response<ResponseModel<LikeModel>> response) {
                        if(response.isSuccessful()){
                            switch (response.body().getSuccess()){
                                case DataDefinition.Network.CODE_SUCCESS:
                                    changeLike(false);
//                                    imageManager.loadImage(context, R.drawable.icon_heart_unclick, IV_like, ImageManager.FIT_TYPE);
//                                    imageManager.loadImage(imageManager.createRequestCreator(context, R.drawable.icon_heart_unclick, ImageManager.FIT_TYPE) .centerCrop(), IV_like);
                                    int likeCount =model.getLikeCount()-1;
                                    TV_like.setText(""+likeCount );
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
            }else {
                Net.getInstance().getFactoryIm().modifyUnLike(new LikeModel(SessionManager.getInstance().getUserModel().getUser_no(), model.getTrip_no())).enqueue(new Callback<ResponseModel<LikeModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<LikeModel>> call, Response<ResponseModel<LikeModel>> response) {
                        if(response.isSuccessful()){
                            switch (response.body().getSuccess()){
                                case DataDefinition.Network.CODE_SUCCESS:
                                    changeLike(true);
//                                    imageManager.loadImage(context, R.drawable.icon_heart_click, IV_like, ImageManager.FIT_TYPE);
//                                    imageManager.loadImage(imageManager.createRequestCreator(context, R.drawable.icon_heart_click, ImageManager.FIT_TYPE), IV_like);
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
