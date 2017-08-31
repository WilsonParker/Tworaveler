package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-01.
 */

public class RegistDayDetailListAdapter extends RecyclerView.Adapter<RegistDayDetailListAdapter.ViewHolder> {
    private ArrayList<ScheduleDayModel> items;
    private Context context;

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
        private ImageView IV_cover;
        private TextView TV_city, TV_time, TV_memo;

        public ViewHolder(View itemView) {
            super(itemView);
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            IV_cover = uiFactory.createView(R.id.item_mypage_detail$IV_cover);
            TV_city = uiFactory.createView(R.id.item_mypage_detail$TV_city);
            TV_time = uiFactory.createView(R.id.item_mypage_detail$TV_time);
            TV_memo = uiFactory.createView(R.id.item_mypage_detail$TV_memo);
        }

        public void toBind(ScheduleDayModel model) {
            ImageManager imageManager = ImageManager.getInstance();
            imageManager.loadImage(context, model.getDtrip_pic_url(), IV_cover, ImageManager.THUMBNAIL_TYPE);
//            TV_city.setText(model.getCity());
            TV_time.setText(model.getStart_time() + " ~ " + model.getEnd_time());
            TV_memo.setText(model.getMemo());
        }
    }

}
