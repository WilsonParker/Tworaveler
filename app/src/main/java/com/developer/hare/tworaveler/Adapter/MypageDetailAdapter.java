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
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.MyScheduleDetailModify;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Listener.OnItemDataChangeListener;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

/**
 * Created by Hare on 2017-08-01.
 */

public class MypageDetailAdapter extends RecyclerView.Adapter<MypageDetailAdapter.ViewHolder> {
    private ArrayList<ScheduleDayModel> items;
    private OnItemDataChangeListener onItemDeleteListener;

    public MypageDetailAdapter(ArrayList<ScheduleDayModel> items, OnItemDataChangeListener onItemDeleteListener) {
        this.items = items;
        this.onItemDeleteListener = onItemDeleteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage_detail, parent, false), parent.getContext());
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
        private Context context;
        private TextView TV_like, TV_commenet, TV_city, TV_address, TV_time, TV_memo;
        private ImageView IV_cover, IV_like, IV_btn;
        private PopupMenu popupMenu;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            IV_cover = uiFactory.createView(R.id.item_mypage_detail$IV_cover);
            TV_city = uiFactory.createView(R.id.item_mypage_detail$TV_city);
            TV_address = uiFactory.createView(R.id.item_mypage_detail$TV_address);
            TV_time = uiFactory.createView(R.id.item_mypage_detail$TV_time);
            TV_memo = uiFactory.createView(R.id.item_mypage_detail$TV_memo);
            TV_like = uiFactory.createView(R.id.item_mypage_detail$TV_like);
            TV_commenet = uiFactory.createView(R.id.item_mypage_detail$TV_comment);
            IV_btn = uiFactory.createView(R.id.item_mypage_detail$IV_more);
            IV_like = uiFactory.createView(R.id.item_mypage_detail$IV_like);
        }

        public void toBind(ScheduleDayModel model) {
            if (!model.getDtrip_pic_url().isEmpty()) {
                ImageManager imageManager = ImageManager.getInstance();
                imageManager.loadImage(imageManager.createRequestCreator(context, model.getDtrip_pic_url(), ImageManager.FIT_TYPE).centerCrop(), IV_cover);
            }
            TV_like.setText(model.getLikeCount() + "");
            TV_commenet.setText(model.getCommentCount() + "");
//            TV_city.setText(model.getTrip_address() + "");
//            TV_address.setText(model.getAddress() + "");
            TV_city.setText(model.getAddress() + "");
            TV_address.setText(model.getTrip_address() + "");
            TV_time.setText(model.getStart_time() + " ~ " + model.getEnd_time());
            TV_memo.setText(model.getMemo() + "");

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
                            switch (item.getItemId()) {
                                case R.id.popup_menu$modify:
                                    intent = new Intent(context, MyScheduleDetailModify.class);
                                    intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, model);
                                    context.startActivity(intent);
                                    break;
                                case R.id.popup_menu$delete:
                                    Net.getInstance().getFactoryIm().deleteDetailTirp(model.getDtrip_no()).enqueue(new Callback<ResponseModel<String>>() {
                                        @Override
                                        public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                                            Log_HR.log(Log_HR.LOG_INFO, MypageDetailAdapter.class, "onResponse", "body : " + response.body().getSuccess());
                                            Log_HR.log(Log_HR.LOG_INFO, MypageDetailAdapter.class, "onResponse", "body : " + response.body().getMessage());
                                            Log_HR.log(Log_HR.LOG_INFO, MypageDetailAdapter.class, "onResponse", "body : " + response.body().getResult());
                                            if (response.isSuccessful()) {
                                                switch (response.body().getSuccess()) {
                                                    case CODE_SUCCESS:
                                                        HandlerManager.getInstance().post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Log_HR.log(Log_HR.LOG_INFO, MypageDetailAdapter.class, "onResponse", "onDelete running");
                                                                items.remove(model);
                                                                onItemDeleteListener.onDelete();
                                                            }
                                                        });
                                                        break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                                            Log_HR.log(MypageDetailAdapter.class, "onFailure", t);
                                        }
                                    });
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }

}
