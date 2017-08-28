package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hare on 2017-08-01.
 */

public class AlamListAdapter extends RecyclerView.Adapter<AlamListAdapter.ViewHolder> {
    private ArrayList<UserModel> items;
    private ImageManager imageManager = ImageManager.getInstance();

    public AlamListAdapter(ArrayList<UserModel> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alam, parent, false), parent.getContext());
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
        private CircleImageView CV_profile;
        private TextView TV_alam;
        private UserModel model;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            CV_profile = uiFactory.createView(R.id.item_alam$CV_profile);
            TV_alam = uiFactory.createView(R.id.item_alam$TV_alam);
            FontManager.getInstance().setFont(TV_alam, "NotoSansCJKkr-Medium.otf");

        }

        public void toBind(UserModel model) {
            this.model = model;
            ImageManager imageManager = ImageManager.getInstance();
            imageManager.loadImage(imageManager.createRequestCreator(context, model.getProfile_pic_url_thumbnail(), ImageManager.FIT_TYPE).placeholder(R.drawable.image_history_profile).centerCrop(), CV_profile);
            TV_alam.setText(model.getNickname()+"님 Tworaveler에 오신걸 환영합니다.");
        }
    }
}