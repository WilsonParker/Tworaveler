package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Model.FeedItemModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-01.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private ArrayList<FeedItemModel> items;

    public HomeListAdapter(ArrayList<FeedItemModel> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage, parent, false), parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "onBindViewHolder(ViewHolder, int)", "binding");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private ImageView IV_cover;
        private TextView TV_date;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            IV_cover = uiFactory.createView(R.id.item_mypage$IV_cover);
            TV_date = uiFactory.createView(R.id.item_mypage$TV_date);
        }

        public void toBind(FeedItemModel model) {
//            Log_HR.log(Log_HR.LOG_INFO, getClass(), "toBint(FeedItemModel)", model.toString());
            ImageManager imageManager = ImageManager.getInstance();
            imageManager.loadImage(context, model.getTrip_pic_url(), IV_cover);
            TV_date.setText(model.getStart_date()+ " ~ "+model.getEnd_date());
        }
    }

}
