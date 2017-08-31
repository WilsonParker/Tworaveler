package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Listener.OnSelectNicknameListener;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;

/**
 * Created by Hare on 2017-08-01.
 */

public class NicknameListAdapter extends RecyclerView.Adapter<NicknameListAdapter.ViewHolder> {
    private OnSelectNicknameListener onSelectNicknameListener;
    private ScheduleModel items;
    private Context context;

    public NicknameListAdapter(OnSelectNicknameListener onSelectNicknameListener, ScheduleModel items, Context context) {
        this.onSelectNicknameListener = onSelectNicknameListener;
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_nickname, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TV_contents;
        private LinearLayout LL_cell;

        public ViewHolder(View itemView) {
            super(itemView);
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            TV_contents = uiFactory.createView(R.id.item_search_nickname$TV_contents);
            LL_cell = uiFactory.createView(R.id.item_search_nickname$LL_cell);
        }

        public void toBind(ScheduleModel model) {
            items = model;
            TV_contents.setText(model.getNickname());
            FontManager.getInstance().setFont(TV_contents, "NotoSansCJKkr-Medium.otf");
            LL_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelectNicknameListener.onSelectNickname(items);
                }
            });
        }
    }

}
