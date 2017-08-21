package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-01.
 */

public class AlertSelectionModeAdapter extends RecyclerView.Adapter<AlertSelectionModeAdapter.ViewHolder> {
    private ArrayList<AlertSelectionItemModel> items;

    public AlertSelectionModeAdapter(ArrayList<AlertSelectionItemModel> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert_selectionmode, parent, false), parent.getContext());
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
        private ImageView IV_icon;
        private TextView TV_text;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            IV_icon = uiFactory.createView(R.id.item_alert_selectionmode$IV_icon);
            TV_text = uiFactory.createView(R.id.item_alert_selectionmode$TV_text);
        }

        public void toBind(AlertSelectionItemModel model) {
            ImageManager.getInstance().loadImage(this.context, model.getImageId(), this.IV_icon, ImageManager.ICON_TYPE);
            IV_icon.setOnClickListener(model.getOnClickListener());
            TV_text.setText(model.getText());
        }
    }
}
