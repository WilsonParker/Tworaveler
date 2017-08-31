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

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Model.CommentModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout LL_more;
        private ImageView IV_btn;
        private CircleImageView IV_profile;
        private TextView TV_nickname, TV_comment, up_btn;
        private EditText ET_comment;
        private CommentModel model;
        private PopupMenu popupMenu;
        private Context context;
        private ProgressManager progressManager;

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
                            switch (item.getItemId()) {
                                case R.id.popup_menu$modify:
                                    ET_comment.setText(model.getContent());
                                    showModifyEditor(true);
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
        }

        private void showModifyEditor(boolean visible){
            if(visible) {
                ET_comment.setVisibility(View.VISIBLE);
                TV_comment.setVisibility(View.INVISIBLE);
                up_btn.setVisibility(View.VISIBLE);
                IV_btn.setVisibility(View.INVISIBLE);
            }else{
                    ET_comment.setVisibility(View.INVISIBLE);
                    TV_comment.setVisibility(View.VISIBLE);
                    up_btn.setVisibility(View.INVISIBLE);
                    IV_btn.setVisibility(View.VISIBLE);
                }
            }

        public void toBind(CommentModel model)
        {
            this.model = model;
            TV_nickname.setText(model.getNickname()+"");
            TV_comment.setText(model.getContent()+"");
            if(SessionManager.getInstance().getUserModel().getNickname().equals(model.getNickname())){
                LL_more.setVisibility(View.VISIBLE);
            }
            up_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeComment();
                }
            });
        }
        public void changeComment(){
            Net.getInstance().getFactoryIm().commentModify(model).enqueue(new Callback<ResponseModel<CommentModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response) {
//                    Log_HR.log(CommentAdapter.class, "onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response)", response);

                    if(response.isSuccessful()){
                        switch (response.body().getSuccess()){
                            case DataDefinition.Network.CODE_SUCCESS:
                                model.setContent(ET_comment.getText().toString());
                                TV_comment.setText(model.getContent()+"");
                                showModifyEditor(false);
                                break;
                        }
                    }else{
                        netFail(R.string.comment_alert_title_fail_3, R.string.comment_alert_content_fail_2);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<CommentModel>> call, Throwable t) {
                    Log_HR.log(CommentAdapter.class, "onFailure(Call<ResponseArrayModel<CommentModel>> call, Throwable t)", t);
                    netFail(R.string.comment_alert_title_fail_3, R.string.comment_alert_content_fail_5);
                }
            });
        }
        private void netFail(int title, int content) {
            progressManager.endRunning();
            AlertManager.getInstance().showNetFailAlert(context, title, content);
        }
    }
}

