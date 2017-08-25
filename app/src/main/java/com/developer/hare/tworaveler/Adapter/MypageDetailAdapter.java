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

import com.developer.hare.tworaveler.Activity.MyScheduleModify;
import com.developer.hare.tworaveler.Listener.OnListScrollListener;
import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.Model.ScheduleDayRootModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-01.
 */

public class MypageDetailAdapter extends RecyclerView.Adapter<MypageDetailAdapter.ViewHolder> {
    private ArrayList<ScheduleDayModel> items;
    private OnListScrollListener onListScrollListener;
    private ScheduleDayRootModel scheduleDayRootModel;

    public MypageDetailAdapter(OnListScrollListener onListScrollListener, ScheduleDayRootModel scheduleDayRootModel) {
        this.onListScrollListener = onListScrollListener;
        this.scheduleDayRootModel = scheduleDayRootModel;
        items = scheduleDayRootModel.getDetaliedTrip();
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
        private TextView TV_date, TV_like, TV_commenet, TV_city, TV_address, TV_time, TV_memo;
        private ImageView IV_cover, IV_like;
        private LinearLayout IV_btn;
        private PopupMenu popupMenu;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            IV_cover = uiFactory.createView(R.id.item_mypage_detail$IV_cover);
            TV_date = uiFactory.createView(R.id.item_mypage_detail$TV_date);
            TV_city = uiFactory.createView(R.id.item_mypage_detail$TV_city);
            TV_address = uiFactory.createView(R.id.item_mypage_detail$TV_address);
            TV_time = uiFactory.createView(R.id.item_mypage_detail$TV_time);
            TV_memo = uiFactory.createView(R.id.item_mypage_detail$TV_memo);
            TV_like = uiFactory.createView(R.id.item_mypage_detail$TV_like);
            TV_commenet = uiFactory.createView(R.id.item_mypage_detail$TV_comment);
            IV_btn = uiFactory.createView(R.id.item_mypage_detail$IV_btn);
            IV_like = uiFactory.createView(R.id.item_mypage_detail$IV_like);


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

        public void toBind(ScheduleDayModel model) {
            ImageManager imageManager = ImageManager.getInstance();
            imageManager.loadImage(imageManager.createRequestCreator(context, model.getdPicture_url(), ImageManager.THUMBNAIL_TYPE).centerCrop(), IV_cover);
            TV_date.setText(model.getTrip_date()+"");
            TV_like.setText(model.getLikeCount() + "");
            TV_commenet.setText(model.getCommentCount() + "");
            TV_city.setText(scheduleDayRootModel.getCity()+"");
            TV_address.setText(model.getAddress()+"");
            TV_time.setText(model.getStartTime() + " ~ " + model.getEndTime());
            TV_memo.setText(model.getMemo()+"");
        }
    }

}