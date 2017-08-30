package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hare on 2017-08-01.
 */

public class FeedNicknameListAdapter extends RecyclerView.Adapter<FeedNicknameListAdapter.ViewHolder> {
    private ArrayList<ScheduleModel> items;
    private Context context;

    public FeedNicknameListAdapter(ArrayList<ScheduleModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_nickname, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TV_contents;
        private CircleImageView CV_profile;
        private LinearLayout LL_cell;

        public ViewHolder(View itemView) {
            super(itemView);
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            TV_contents = uiFactory.createView(R.id.item_search_nickname$TV_contents);
            CV_profile = uiFactory.createView(R.id.item_search_nickname$CV_profile);
            LL_cell = uiFactory.createView(R.id.item_search_nickname$LL_cell);
        }

        public void toBind(ScheduleModel model) {

            TV_contents.setText(model.getCity());
            FontManager.getInstance().setFont(TV_contents, "NotoSansCJKkr-Medium.otf");
            LL_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

}