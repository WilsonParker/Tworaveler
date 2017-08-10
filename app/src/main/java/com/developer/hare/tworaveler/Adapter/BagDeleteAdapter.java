package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.developer.hare.tworaveler.Model.BagDeleteModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

public class BagDeleteAdapter extends RecyclerView.Adapter<BagDeleteAdapter.ViewHolder> {
    private ArrayList<BagDeleteModel> items, selected_items;
    private Context context;

    public BagDeleteAdapter(ArrayList<BagDeleteModel> items, ArrayList<BagDeleteModel> selected_items, Context context) {
        this.items = items;
        this.selected_items = selected_items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bag_delete_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<BagDeleteModel> getSelected_Items(){
        return  this.selected_items;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView RV_image;
        private CheckBox CB;
        private BagDeleteModel model;

        private View.OnClickListener checkBoxClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setChecked(!model.isChecked());
                CB.setChecked(model.isChecked());
                if (model.isChecked())
                    selected_items.add(model);
                else {
                    if (selected_items.contains(model))
                        selected_items.remove(model);
                }

            }
        };

        public ViewHolder(View itemView) {
            super(itemView);
            RV_image = itemView.findViewById(R.id.item_bag_delete$IV);
            RV_image.setOnClickListener(checkBoxClickListener);
            CB = itemView.findViewById(R.id.item_bag_delete$CB);
            CB.setOnClickListener(checkBoxClickListener);
        }

        public void toBind(BagDeleteModel model) {
            this.model = model;
            CB.setChecked(model.isChecked());
            ImageManager imageManager = ImageManager.getInstance();
            imageManager.loadImage(context, model.getImage(), RV_image);
        }
    }
}
