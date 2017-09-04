package com.developer.hare.tworaveler.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.CommentAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.Page.FragmentFeedSchedule;
import com.developer.hare.tworaveler.Fragment.Page.FragmentMyPageSchedule;
import com.developer.hare.tworaveler.Listener.OnItemDataChangeListener;
import com.developer.hare.tworaveler.Listener.OnModifyListener;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.CommentModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.FontManager;
import com.developer.hare.tworaveler.UI.KeyboardManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.developer.hare.tworaveler.Util.ResourceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_FEED;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_MYPAGE;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_STARTED_BY;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class Comment extends AppCompatActivity {

    private RecyclerView RV_commentlist;
    private MenuTopTitle menuToptitle;
    private LinearLayout LL_noitem, LL_comment_write;
    private TextView TV_noitem, up_btn;
    private EditText ET_comment;

    private UserModel userModel;
    private int started;
    private UIFactory uiFactory;
    private ProgressManager progressManager;
    private ResourceManager resourceManager;
    private KeyboardManager keyboardManager;
    private ArrayList<CommentModel> items = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private ScheduleModel scheduleModel;
    private OnItemDataChangeListener onItemDeleteListener = new OnItemDataChangeListener() {
        @Override
        public void onChange() {
            createCommentList();
        }
    };

    private OnModifyListener onModifyListener = new OnModifyListener() {
        @Override
        public void onEditing() {
            LL_comment_write.setVisibility(View.GONE);
        }

        @Override
        public void onComplete() {
            LL_comment_write.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createCommentList();
        changeView();
        if (!sessionCheck()) {
            ET_comment.setHint(resourceManager.getResourceString(R.string.comment_not_login_editText_message));
            ET_comment.setEnabled(false);
        }
    }

    private void init() {
        scheduleModel = (ScheduleModel) getIntent().getExtras().get(DataDefinition.Intent.KEY_SCHEDULE_MODEL);
        started = getIntent().getIntExtra(KEY_STARTED_BY, -1);
        uiFactory = UIFactory.getInstance(this);
        progressManager = new ProgressManager(this);
        resourceManager = ResourceManager.getInstance();
        keyboardManager = new KeyboardManager();

        RV_commentlist = uiFactory.createView(R.id.activity_comment$RV_commentlist);
        LL_noitem = uiFactory.createView(R.id.activity_comment$LL_noitem);
        LL_comment_write = uiFactory.createView(R.id.activity_comment$LL_comment_write);
        TV_noitem = uiFactory.createView(R.id.activity_comment$TV_noitem);
        up_btn = uiFactory.createView(R.id.activity_comment$up_btn);
        ET_comment = uiFactory.createView(R.id.activity_comment$ET_comment);
        menuToptitle = uiFactory.createView(R.id.activity_comment$menuToptitle);
        FontManager.getInstance().setFont(TV_noitem, "NotoSansCJKkr-Regular.otf");

        menuToptitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (started) {
                    case KEY_FEED:
                        FragmentFeedSchedule.newInstance(scheduleModel);
                        break;
                    case KEY_MYPAGE:
                        FragmentMyPageSchedule.newInstance(scheduleModel);
                        break;
                }
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Comment.this, LinearLayoutManager.VERTICAL, false);
        RV_commentlist.setLayoutManager(linearLayoutManager);

        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendComment(view);
            }
        });

    }

    private void changeView() {
        if (scheduleModel.getCommentCount() == 0) {
            LL_noitem.setVisibility(View.VISIBLE);
            RV_commentlist.setVisibility(View.INVISIBLE);
        } else {
            LL_noitem.setVisibility(View.INVISIBLE);
            RV_commentlist.setVisibility(View.VISIBLE);
        }
    }

    public void onSendComment(View view) {
        if (!sessionCheck()) {
            AlertManager.getInstance().showNotLoginAlert(Comment.this, R.string.comment_alert_title_fail);
            return;
        }
        String msg = ET_comment.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            ET_comment.setError("글을 작성 해주세요");
            return;
        }
        CommentModel commentModel = new CommentModel(scheduleModel.getTrip_no(), SessionManager.getInstance().getUserModel().getNickname(), msg);
        Net.getInstance().getFactoryIm().commentUpload(commentModel).enqueue(new Callback<ResponseModel<CommentModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response) {
//                Log_HR.log(Comment.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                if (response.isSuccessful()) {
                    ResponseModel<CommentModel> model = response.body();
                    if (model.getSuccess() == CODE_SUCCESS) {
                        HandlerManager.getInstance().post(new Runnable() {
                            @Override
                            public void run() {
                                ET_comment.setText("");
                                items.add(commentModel);
                                commentAdapter.notifyDataSetChanged();
                                RV_commentlist.scrollToPosition(items.size() - 1);
                                scheduleModel.setCommentCount(scheduleModel.getCommentCount() + 1);
                                keyboardManager.dismissInputKeyboard(getApplicationContext());
                                changeView();
                            }
                        });
                    } else {
                        Log_HR.log(Log_HR.LOG_WARN, Comment.class, "onResponse", "onResponse is not successful");
                        netFail(R.string.comment_alert_title_fail, R.string.comment_alert_content_fail);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<CommentModel>> call, Throwable t) {
                netFail(R.string.comment_alert_title_fail, R.string.comment_alert_content_fail_5);
                Log_HR.log(Comment.class, "onFailure(Call<ResponseModel<CommentModel>> call, Throwable t)", t);
            }
        });

    }

    public void createCommentList() {
        progressManager.actionWithState(new OnProgressAction() {
            @Override
            public void run() {
                Net.getInstance().getFactoryIm().commentList(scheduleModel.getTrip_no()).enqueue(new Callback<ResponseArrayModel<CommentModel>>() {
                    @Override
                    public void onResponse(Call<ResponseArrayModel<CommentModel>> call, Response<ResponseArrayModel<CommentModel>> response) {
                        if (response.isSuccessful()) {
                            progressManager.endRunning();
                            ResponseArrayModel<CommentModel> model = response.body();
                            if (model.getSuccess() == CODE_SUCCESS) {
                                HandlerManager.getInstance().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        items = model.getResult();
                                        commentAdapter = new CommentAdapter(items, CommentAdapter.COMMENT, onItemDeleteListener, onModifyListener);
                                        RV_commentlist.setAdapter(commentAdapter);
                                        commentAdapter.notifyDataSetChanged();
                                    }
                                });
                            } else {
                                netFail(R.string.comment_alert_title_fail_2, R.string.comment_alert_content_fail_2);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseArrayModel<CommentModel>> call, Throwable t) {
                        netFail(R.string.comment_alert_title_fail_2, R.string.comment_alert_content_fail_5);
                        Log_HR.log(Comment.class, "onFailure(Call<ResponseModel<CommentModel>> call, Throwable t)", t);
                    }
                });
            }
        });
    }

    private void netFail(int title, int content) {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(this, title, content);
    }

    private boolean sessionCheck() {
        userModel = SessionManager.getInstance().getUserModel();
        return SessionManager.getInstance().isLogin();
    }
}
