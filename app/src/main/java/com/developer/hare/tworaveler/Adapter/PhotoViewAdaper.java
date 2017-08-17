package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Hare on 2017-08-09.
 */

public class PhotoViewAdaper extends RecyclerView.Adapter<PhotoViewAdaper.ViewHolder> {
    private ArrayList<BagModel> items;
    private Context context;

    public PhotoViewAdaper(ArrayList<BagModel> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photoview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private PhotoView PV_image;
        public ViewHolder(View itemView) {
            super(itemView);
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            PV_image = uiFactory.createView(R.id.item_photoview$PV_image);
        }

        public void toBind(BagModel model) {
            ImageManager imageManager = ImageManager.getInstance();
            if (model.isFile())
                imageManager.loadImage(imageManager.createRequestCreator(context, model.getFile()).centerCrop(), PV_image);
            else
                imageManager.loadImage(imageManager.createRequestCreator(context, model.getCategory_pic_url()), PV_image);
        }
    }
}
