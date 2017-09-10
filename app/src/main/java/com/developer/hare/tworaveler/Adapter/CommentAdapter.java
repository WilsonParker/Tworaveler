package com.developer.hare.tworaveler.Adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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
import com.developer.hare.tworaveler.Listener.OnItemDataChangeListener;
import com.developer.hare.tworaveler.Listener.OnModifyListener;
import com.developer.hare.tworaveler.Model.CommentModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.KeyboardManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Exception.NullChecker;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.LogManager;

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
    private int type;
    private OnItemDataChangeListener onItemDeleteListener;
    private OnModifyListener onModifyListener;
    private ViewHolder editing_holder;
    private KeyboardManager keyboardManager;
    public static final int COMMENT = 0x0001, COMMENT_DETAIL = 0x0010;

    public CommentAdapter(ArrayList<CommentModel> items, int type, OnItemDataChangeListener onItemDeleteListener, OnModifyListener onModifyListener) {
        this.items = items;
        this.type = type;
        this.onItemDeleteListener = onItemDeleteListener;
        this.onModifyListener = onModifyListener;
        keyboardManager = new KeyboardManager();
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
                                    if (editing_holder != null)
                                        editing_holder.showModifyEditor(false);
                                    editing_holder = ViewHolder.this;
                                    ET_comment.findFocus();
                                    keyboardManager.showInputKeyboard(context);
                                    ET_comment.setText(model.getContent());
//                                    LogManager.log(LogManager.LOG_INFO,CommentAdapter.class,"ET_comment","count" + ET_comment.getLineCount());
                                    ET_comment.setOnKeyListener(new View.OnKeyListener() {
                                        @Override
                                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                            if (ET_comment.getLineCount() == 3) {
                                                if (i == keyEvent.KEYCODE_ENTER) {
                                                    return true;
                                                }
                                            }
                                            return false;
                                        }
                                    });
                                    showModifyEditor(true);
                                    onModifyListener.onEditing();
                                    break;
                                case R.id.popup_menu$delete:
                                    commentDelete();
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        private void showModifyEditor(boolean visible) {
            if (visible) {
                ET_comment.setVisibility(View.VISIBLE);
                TV_comment.setVisibility(View.INVISIBLE);
                up_btn.setVisibility(View.VISIBLE);
                IV_btn.setVisibility(View.INVISIBLE);
            } else {
                ET_comment.setVisibility(View.INVISIBLE);
                TV_comment.setVisibility(View.VISIBLE);
                up_btn.setVisibility(View.INVISIBLE);
                IV_btn.setVisibility(View.VISIBLE);
            }
        }

        public void toBind(CommentModel model) {
            this.model = model;
            TV_nickname.setText(model.getNickname() + "");
            TV_comment.setText(model.getContent() + "");
            if (SessionManager.getInstance().isLogin() && SessionManager.getInstance().getUserModel().getUser_no() == model.getUser_no()) {
                LL_more.setVisibility(View.VISIBLE);
            }
            ImageManager imageManager = ImageManager.getInstance();
            if (NullChecker.getInstance().nullCheck(model.getProfile_pic_thumbnail_url())) {
                imageManager.loadImage(imageManager.createRequestCreator(context, R.drawable.image_profile, ImageManager.BASIC_TYPE), IV_profile);
            } else {
                imageManager.loadImage(imageManager.createRequestCreator(context, model.getProfile_pic_thumbnail_url(), ImageManager.THUMBNAIL_TYPE).centerInside().placeholder(R.drawable.image_profile), IV_profile);
            }
            up_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeComment();
                }
            });
        }

        private void changeComment() {
            model.setContent(ET_comment.getText().toString());
            LogManager.log(LogManager.LOG_INFO, CommentAdapter.class, "onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response)", "CommentModel : " + model);

            switch (type) {
                case COMMENT:
                    Net.getInstance().getFactoryIm().commentModify(model).enqueue(new Callback<ResponseModel<CommentModel>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response) {
                            LogManager.log(CommentAdapter.class, "onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response)", response);
                            if (response.isSuccessful()) {
                                switch (response.body().getSuccess()) {
                                    case DataDefinition.Network.CODE_SUCCESS:
                                        model.setContent(ET_comment.getText().toString());
                                        TV_comment.setText(model.getContent() + "");
                                        showModifyEditor(false);
                                        onModifyListener.onComplete();
                                        keyboardManager.dismissInputKeyboard(context);
                                        break;
                                    case DataDefinition.Network.CODE_ERROR:
                                        netFail(R.string.comment_alert_title_fail_3, R.string.comment_alert_content_fail_5);
                                        break;
                                }
                            } else {
                                netFail(R.string.comment_alert_title_fail_3, R.string.comment_alert_content_fail_2);
                                LogManager.log(LogManager.LOG_INFO, CommentAdapter.class, "onResponse", "response is not successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<CommentModel>> call, Throwable t) {
                            netFail(R.string.comment_alert_title_fail_3, R.string.comment_alert_content_fail_5);
                            LogManager.log(CommentAdapter.class, "onFailure", t);
                        }
                    });
                    break;
                case COMMENT_DETAIL:
                    Net.getInstance().getFactoryIm().commentDetailModify(model).enqueue(new Callback<ResponseModel<CommentModel>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response) {
                            LogManager.log(CommentAdapter.class, "onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response)", response);
                            if (response.isSuccessful()) {
                                switch (response.body().getSuccess()) {
                                    case DataDefinition.Network.CODE_SUCCESS:
                                        model.setContent(ET_comment.getText().toString());
                                        TV_comment.setText(model.getContent() + "");
                                        showModifyEditor(false);
                                        keyboardManager.dismissInputKeyboard(context);
                                        break;
                                    case DataDefinition.Network.CODE_ERROR:
                                        netFail(R.string.comment_alert_title_fail_3, R.string.comment_alert_content_fail_5);
                                        break;
                                }
                            } else {
                                netFail(R.string.comment_detail_alert_title_fail_3, R.string.comment_alert_content_fail_2);
                                LogManager.log(LogManager.LOG_INFO, CommentAdapter.class, "onResponse", "response is not successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<CommentModel>> call, Throwable t) {
                            netFail(R.string.comment_detail_alert_title_fail_3, R.string.comment_alert_content_fail_5);
                            LogManager.log(CommentAdapter.class, "onFailure", t);
                        }
                    });
                    break;
            }
        }

        private void commentDelete() {
            switch (type) {
                case COMMENT:
                    Net.getInstance().getFactoryIm().commentDelete(model).enqueue(new Callback<ResponseModel<String>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                            LogManager.log(CommentAdapter.class, "onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response)", response);
                            if (response.isSuccessful()) {
                                switch (response.body().getSuccess()) {
                                    case DataDefinition.Network.CODE_SUCCESS:
                                        HandlerManager.getInstance().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                items.remove(model);
                                                onItemDeleteListener.onChange();
                                            }
                                        });
                                        break;
                                    case DataDefinition.Network.CODE_ERROR:
                                        netFail(R.string.comment_alert_title_fail_4, R.string.comment_alert_content_fail_5);
                                        break;
                                }
                            } else {
                                netFail(R.string.comment_alert_title_fail_4, R.string.comment_alert_content_fail_4);
                                LogManager.log(LogManager.LOG_INFO, CommentAdapter.class, "onResponse", "response is not successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                            netFail(R.string.comment_alert_title_fail_4, R.string.comment_alert_content_fail_5);
                            LogManager.log(CommentAdapter.class, "onFailure", t);
                        }
                    });
                    break;
                case COMMENT_DETAIL:
                    Net.getInstance().getFactoryIm().commentDetailDelete(model).enqueue(new Callback<ResponseModel<CommentModel>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response) {
                            LogManager.log(CommentAdapter.class, "onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response)", response);
                            if (response.isSuccessful()) {
                                if (response.body().getSuccess() == DataDefinition.Network.CODE_SUCCESS) {
                                    HandlerManager.getInstance().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            items.remove(model);
                                            onItemDeleteListener.onChange();
                                        }
                                    });
                                }
                            } else {
                                netFail(R.string.comment_detail_alert_title_fail_4, R.string.comment_alert_content_fail_4);
                                LogManager.log(LogManager.LOG_INFO, CommentAdapter.class, "onResponse", "response is not successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<CommentModel>> call, Throwable t) {
                            netFail(R.string.comment_detail_alert_title_fail_4, R.string.comment_alert_content_fail_5);
                            LogManager.log(CommentAdapter.class, "onFailure", t);
                        }
                    });
                    break;
            }
        }

        private void netFail(int title, int content) {
            AlertManager.getInstance().showNetFailAlert(context, title, content);
        }
    }
}


