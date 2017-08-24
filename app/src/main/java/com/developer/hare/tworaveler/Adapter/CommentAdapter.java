package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Model.CommentModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-08-23.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<CommentModel> items;

    public CommentAdapter(ArrayList<CommentModel> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false), parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.toBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout LL_more;
        private ImageView IV_profile, IV_btn;
        private TextView TV_nickname, TV_comment, up_btn;
        private EditText ET_comment;
        private CommentModel model;
        private PopupMenu popupMenu;
        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            UIFactory uiFactory = UIFactory.getInstance(itemView);
            LL_more = uiFactory.createView(R.id.item_comment$LL_more);
            IV_profile = uiFactory.createView(R.id.item_comment$IV_profile);
            TV_nickname = uiFactory.createView(R.id.item_comment$TV_nickname);
            TV_comment = uiFactory.createView(R.id.item_comment$TV_comment);
            up_btn = uiFactory.createView(R.id.item_comment$up_btn);
            ET_comment = uiFactory.createView(R.id.item_comment$ET_comment);
            IV_btn = uiFactory.createView(R.id.item_comment$IV_btn);


            LL_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMenu = new PopupMenu(context, view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    Menu menu = popupMenu.getMenu();

                    inflater.inflate(R.menu.popup_menu, menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.popup_menu$modify:

                                    ET_comment.setVisibility(View.VISIBLE);
                                    TV_comment.setVisibility(View.INVISIBLE);
                                    up_btn.setVisibility(View.VISIBLE);
                                    IV_btn.setVisibility(View.INVISIBLE);
                                    break;
                                case R.id.popup_menu$delete:

                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            up_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ET_comment.setVisibility(View.INVISIBLE);
                    TV_comment.setVisibility(View.VISIBLE);
                    up_btn.setVisibility(View.INVISIBLE);
                    IV_btn.setVisibility(View.VISIBLE);
                    TV_comment.setText(model.getContent()+"");
                }
            });
        }

        public void toBind(CommentModel model)
        {
            this.model = model;
            TV_nickname.setText(model.getNickname()+"");
            TV_comment.setText(model.getContent()+"");
        }
    }
}
