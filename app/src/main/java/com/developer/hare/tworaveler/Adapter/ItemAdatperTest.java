package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-13.
 */

public class ItemAdatperTest extends BaseAdapter{
    private ArrayList<AlertSelectionItemModel> items;
    private Context context;

    public ItemAdatperTest(ArrayList<AlertSelectionItemModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_alert_selectionmode, viewGroup, false);
        ViewHolder holder = new ViewHolder(view, context);
        holder.toBind(items.get(i));
        return holder;
    }

    class ViewHolder extends View{
        private Context context;
        private ImageView IV_icon;
        private TextView TV_text;

        public ViewHolder(View itemView, Context context) {
            super(context);
            this.context = context;
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            IV_icon = uiFactory.createView(R.id.item_alert_selectionmode$IV_icon);
            TV_text = uiFactory.createView(R.id.item_alert_selectionmode$TV_text);
        }

        public void toBind(AlertSelectionItemModel model) {
            ImageManager.getInstance().loadImage(this.context, model.getImageId(), this.IV_icon);
            IV_icon.setOnClickListener(model.getOnClickListener());
            TV_text.setText(model.getText());
        }
    }
}
