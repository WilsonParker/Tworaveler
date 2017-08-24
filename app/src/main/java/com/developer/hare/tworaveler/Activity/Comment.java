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
import android.widget.Toast;

import com.developer.hare.tworaveler.Adapter.CommentAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Model.CommentModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class Comment extends AppCompatActivity {

    private RecyclerView RV_commentlist;
    private MenuTopTitle menuToptitle;
    private LinearLayout LL_noitem;
    private TextView TV_noitem, up_btn;
    private EditText ET_comment;
    private UIFactory uiFactory;
    private ArrayList<CommentModel> items = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private ScheduleModel scheduleModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
    }

    @Override
    protected void onResume() {
        createCommentList();
        super.onResume();
    }

    private void init()
    {
        scheduleModel = (ScheduleModel) getIntent().getExtras().get(DataDefinition.Intent.KEY_SCHEDULE_MODEL);
        uiFactory = UIFactory.getInstance(this);
        RV_commentlist = uiFactory.createView(R.id.activity_comment$RV_commentlist);
        LL_noitem   = uiFactory.createView(R.id.activity_comment$LL_noitem);
        TV_noitem   = uiFactory.createView(R.id.activity_comment$TV_noitem);
        up_btn      = uiFactory.createView(R.id.activity_comment$up_btn);
        ET_comment  = uiFactory.createView(R.id.activity_comment$ET_comment);
        menuToptitle = uiFactory.createView(R.id.activity_comment$menuToptitle);
        FontManager.getInstance().setFont(TV_noitem, "NotoSansCJKkr-Regular.otf");

        menuToptitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Comment.this, LinearLayoutManager.VERTICAL, false);
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
    public void onSendComment(View view){
        String msg = ET_comment.getText().toString().trim();
        if(TextUtils.isEmpty(msg)){
            ET_comment.setError("글을 작성 해주세요");
            return;
        }
        CommentModel commentModel = new CommentModel(scheduleModel.getTrip_no(), SessionManager.getInstance().getUserModel().getNickname(), msg);
        Call<ResponseModel<CommentModel>> result = Net.getInstance().getFactoryIm().commentUpload(commentModel);
        result.enqueue(new Callback<ResponseModel<CommentModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response) {
                if(response.isSuccessful()){
                    ResponseModel<CommentModel> model = response.body();
                    if(model.getSuccess() == CODE_SUCCESS){
                        HandlerManager.getInstance().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Comment.this, "글이 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                                ET_comment.setText("");
                                items.add(commentModel);
                                commentAdapter.notifyDataSetChanged();
                            }
                        });
                    }else {
                                Toast.makeText(Comment.this, "등록 실패.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<CommentModel>> call, Throwable t) {
                                Toast.makeText(Comment.this, "Comment onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void createCommentList(){

        Net.getInstance().getFactoryIm().commentList(scheduleModel.getTrip_no()).enqueue(new Callback<ResponseArrayModel<CommentModel>>() {
            @Override
            public void onResponse(Call<ResponseArrayModel<CommentModel>> call, Response<ResponseArrayModel<CommentModel>> response) {
                if(response.isSuccessful()){
                    ResponseArrayModel<CommentModel> model = response.body();
                    if(model.getSuccess() == CODE_SUCCESS){
                        items.addAll(model.getResult());
                        commentAdapter = new CommentAdapter(items);
                        RV_commentlist.setAdapter(commentAdapter);
                    }else{
                        Toast.makeText(Comment.this, "덧글 불러오기 실패.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayModel<CommentModel>> call, Throwable t) {
                Log_HR.log(Comment.class, "onFailure()", t);
                Toast.makeText(Comment.this, "CreatList onFailure", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
