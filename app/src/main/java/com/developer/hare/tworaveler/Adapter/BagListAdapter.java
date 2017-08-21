package com.developer.hare.tworaveler.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.developer.hare.tworaveler.Activity.BigImage;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-01.
 */

public class BagListAdapter extends RecyclerView.Adapter<BagListAdapter.ViewHolder> {
    private ArrayList<BagModel> items;
    private Activity activity;

    public BagListAdapter(ArrayList<BagModel> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_bag_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
        holder.cardVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(activity, position+"번쨰 카드 클릭", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, BigImage.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(DataDefinition.Intent.KEY_BAGMODEL, items.get(position));
                intent.putExtra(DataDefinition.Intent.KEY_BAGMODELS, items);
                intent.putExtra(DataDefinition.Intent.KEY_POSITION, position);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView RV_image;
        CardView cardVIew;

        public ViewHolder(View itemView) {
            super(itemView);
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            RV_image = uiFactory.createView(R.id.bag_cell_image);
            cardVIew = uiFactory.createView(R.id.cardVIew);
        }

        public void toBind(BagModel model) {
            ImageManager imageManager = ImageManager.getInstance();
            if (model.isFile())
                imageManager.loadImage(imageManager.createRequestCreator(activity, model.getFile(), ImageManager.FIT_TYPE).centerCrop(), RV_image);
            else
                imageManager.loadImage(imageManager.createRequestCreator(activity, model.getCategory_pic_thumbnail_url(), ImageManager.FIT_TYPE), RV_image);
        }

    }

}
