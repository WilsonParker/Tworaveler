package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-01.
 */

public class RegistDayDetailListAdapter extends RecyclerView.Adapter<RegistDayDetailListAdapter.ViewHolder> {
    private ArrayList<ScheduleDayModel> items;
    private Context context;
    private ImageManager imageManager = ImageManager.getInstance();

    public RegistDayDetailListAdapter(ArrayList<ScheduleDayModel> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_mypage_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView IV_cover, IV_like,IV_comment;
        private TextView TV_city, TV_time, TV_memo,TV_like, TV_comment;
        private LinearLayout LL_like, LL_comment;
        private ScheduleDayModel model;

        public ViewHolder(View itemView) {
            super(itemView);
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            IV_cover = uiFactory.createView(R.id.item_mypage_detail$IV_cover);
            TV_city = uiFactory.createView(R.id.item_mypage_detail$TV_city);
            TV_time = uiFactory.createView(R.id.item_mypage_detail$TV_time);
            TV_memo = uiFactory.createView(R.id.item_mypage_detail$TV_memo);
            IV_like = uiFactory.createView(R.id.item_mypage_detail$IV_like);
            IV_comment = uiFactory.createView(R.id.item_mypage_detail$IV_comment);
            TV_like = uiFactory.createView(R.id.item_mypage_detail$TV_like);
            TV_comment = uiFactory.createView(R.id.item_mypage_detail$TV_comment);
            LL_like = uiFactory.createView(R.id.item_mypage_detail$LL_like);
            LL_comment = uiFactory.createView(R.id.item_mypage_detail$LL_comment);

            FontManager.getInstance().setFont(TV_city, "NotoSansCJKkr-Bold.otf");
            ArrayList<TextView> textlist1 = new ArrayList<>();
            textlist1.add(TV_like);
            textlist1.add(TV_comment);
            FontManager.getInstance().setFont(textlist1, "Roboto-Medium.ttf");
        }

        public void toBind(ScheduleDayModel model) {
            this.model = model;
            TV_like.setText(model.getLikeCount()+"");
            TV_comment.setText(model.getCommentCount()+"");
            TV_time.setText(model.getStart_time() + " ~ " + model.getEnd_time());
            TV_memo.setText(model.getMemo());
            /*LL_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeClick(model.isLike());
                }
            });
            changeLike(model.isLike());*/
        }
        /*private void changeLike(boolean isLike){
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
        }*/
    }

}
