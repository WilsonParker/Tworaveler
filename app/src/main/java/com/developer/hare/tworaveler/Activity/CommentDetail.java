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
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.CommentModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class CommentDetail extends AppCompatActivity {

    private RecyclerView RV_commentlist;
    private MenuTopTitle menuToptitle;
    private LinearLayout LL_noitem;
    private TextView TV_noitem, up_btn;
    private EditText ET_comment;

    private UIFactory uiFactory;
    private ProgressManager progressManager;
    private ArrayList<CommentModel> items = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private ScheduleDayModel scheduleDayModel;

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
    }

    private void init() {
        scheduleDayModel = (ScheduleDayModel) getIntent().getExtras().get(DataDefinition.Intent.KEY_SCHEDULE_DAY_MODEL);
        uiFactory = UIFactory.getInstance(this);
        progressManager = new ProgressManager(this);

        RV_commentlist = uiFactory.createView(R.id.activity_comment$RV_commentlist);
        LL_noitem = uiFactory.createView(R.id.activity_comment$LL_noitem);
        TV_noitem = uiFactory.createView(R.id.activity_comment$TV_noitem);
        up_btn = uiFactory.createView(R.id.activity_comment$up_btn);
        ET_comment = uiFactory.createView(R.id.activity_comment$ET_comment);
        menuToptitle = uiFactory.createView(R.id.activity_comment$menuToptitle);
        FontManager.getInstance().setFont(TV_noitem, "NotoSansCJKkr-Regular.otf");

        menuToptitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentDetail.this, LinearLayoutManager.VERTICAL, false);
        RV_commentlist.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(items);
        RV_commentlist.setAdapter(commentAdapter);

        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendComment(view);
            }
        });
    }

    private void changeView() {
        if (scheduleDayModel.getCommentCount() == 0) {
            LL_noitem.setVisibility(View.VISIBLE);
            RV_commentlist.setVisibility(View.INVISIBLE);
        } else {
            LL_noitem.setVisibility(View.INVISIBLE);
            RV_commentlist.setVisibility(View.VISIBLE);
        }
    }

    public void onSendComment(View view) {
        String msg = ET_comment.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            ET_comment.setError("글을 작성 해주세요");
            return;
        }
        CommentModel commentModel = new CommentModel(scheduleDayModel.getTrip_no(), scheduleDayModel.getDtrip_no(), SessionManager.getInstance().getUserModel().getNickname(), msg);
        Net.getInstance().getFactoryIm().commentDetailUpload(commentModel).enqueue(new Callback<ResponseModel<CommentModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response) {
//                Log_HR.log(Log_HR.LOG_INFO,Comment.class, "onResponse","body : "+response.body().getSuccess());
//                Log_HR.log(Log_HR.LOG_INFO,Comment.class, "onResponse","body : "+response.body().getMessage());
//                Log_HR.log(Log_HR.LOG_INFO,Comment.class, "onResponse","body : "+response.body().getResult());
                if (response.isSuccessful()) {
                    ResponseModel<CommentModel> model = response.body();
                    if (model.getSuccess() == CODE_SUCCESS) {
                        HandlerManager.getInstance().post(new Runnable() {
                            @Override
                            public void run() {
                                Log_HR.log(Log_HR.LOG_INFO, CommentDetail.class, "onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response)", "items Size : " + items.size());
//                                Toast.makeText(Comment.this, "글이 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                                ET_comment.setText("");
                                items.add(commentModel);
                                commentAdapter.notifyDataSetChanged();
                                RV_commentlist.scrollToPosition(items.size() - 1);
                                scheduleDayModel.setCommentCount(scheduleDayModel.getCommentCount()+1);
                                changeView();
                            }
                        });
                    } else {
                        netFail(R.string.comment_alert_title_fail, R.string.comment_alert_content_fail);
//                        Toast.makeText(Comment.this, "등록 실패.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<CommentModel>> call, Throwable t) {
                netFail(R.string.comment_alert_title_fail, R.string.comment_alert_content_fail_5);
//                Toast.makeText(Comment.this, "Comment onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void createCommentList() {
        progressManager.actionWithState(new OnProgressAction() {
            @Override
            public void run() {
                Net.getInstance().getFactoryIm().commentDetailList(scheduleDayModel.getDtrip_no(), scheduleDayModel.getTrip_date()).enqueue(new Callback<ResponseArrayModel<CommentModel>>() {
                    @Override
                    public void onResponse(Call<ResponseArrayModel<CommentModel>> call, Response<ResponseArrayModel<CommentModel>> response) {
                    Log_HR.log(Log_HR.LOG_INFO, CommentDetail.class, "댓글 리스트 불러오기", "items Size : " + response.isSuccessful());
                    Log_HR.log(Log_HR.LOG_INFO, CommentDetail.class, "댓글 리스트 불러오기", "items Size : " + response.message());
                        response.body().getResult().forEach((model) -> Log_HR.log(Log_HR.LOG_INFO, CommentDetail.class, "댓글 리스트 불러오기", "items Size : " +model));
                        if (response.isSuccessful()) {
                            progressManager.endRunning();
                            ResponseArrayModel<CommentModel> model = response.body();
                            if (model.getSuccess() == CODE_SUCCESS) {
                                HandlerManager.getInstance().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        items = model.getResult();
//                                        commentAdapter.notifyDataSetChanged();
//                                        RV_commentlist.setAdapter(new CommentAdapter(items));
                                        commentAdapter = new CommentAdapter(items);
                                        RV_commentlist.setAdapter(commentAdapter);
                                        commentAdapter.notifyDataSetChanged();
                                    }
                                });
                            } else {
                                netFail(R.string.comment_alert_title_fail_2, R.string.comment_alert_content_fail_2);
//                        Toast.makeText(Comment.this, "덧글 불러오기 실패.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseArrayModel<CommentModel>> call, Throwable t) {
                        Log_HR.log(CommentDetail.class, "onFailure()", t);
                        netFail(R.string.comment_alert_title_fail_2, R.string.comment_alert_content_fail_5);
//                Toast.makeText(Comment.this, "CreatList onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void netFail(int title, int content) {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(this, title, content);
    }
}
