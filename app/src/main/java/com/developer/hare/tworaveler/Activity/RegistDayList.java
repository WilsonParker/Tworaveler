package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.RegistDayDetailListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistDayList extends AppCompatActivity {

    private UIFactory uiFactory;
    private DateManager dateManager;
    private ProgressManager progressManager;
    private Intent intent;
    private String selected_date;
    private ScheduleModel scheduleModel;
    private UserModel userModel;

    private RegistDayDetailListAdapter registDayDetailListAdapter;
    private MenuTopTitle menuTopTitle;
    private TextView TV_date;
    private ImageView IV_noData;
    private RecyclerView recyclerView;
    private LinearLayout LL_empty, LL_list;
    private ArrayList<ScheduleDayModel> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_day_detail_list);
        init();
    }

    protected void init() {
        intent = getIntent();
        selected_date = intent.getExtras().getString(DataDefinition.Intent.KEY_DATE);
        scheduleModel = (ScheduleModel) intent.getSerializableExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL);

        uiFactory = UIFactory.getInstance(this);
        dateManager = DateManager.getInstance();
        progressManager = new ProgressManager(this);

        LL_empty = uiFactory.createView(R.id.activity_regist_day_detail_list$LL_empty);
        LL_list = uiFactory.createView(R.id.activity_regist_day_detail_list$LL_list);
        TV_date = uiFactory.createView(R.id.activity_regist_day_detail_list$TV_date);
        TV_date.setText(selected_date);
        IV_noData = uiFactory.createView(R.id.activity_regist_day_detail_list$IV_nologin);
        IV_noData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister();
            }
        });
        sessionCheck();
        menuTopTitle = uiFactory.createView(R.id.activity_regist_day_detail_list$menuTopTItle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister();
            }
        });
        menuTopTitle.getTV_title().setText(userModel.getNickname());
        recyclerView = uiFactory.createView(R.id.activity_regist_day_detail_list$RV_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        registDayDetailListAdapter = new RegistDayDetailListAdapter(items);
        createList();
    }

    private void initLayout() {
        if (items.isEmpty()) {
            LL_empty.setVisibility(View.VISIBLE);
            LL_list.setVisibility(View.GONE);
        } else {
            LL_empty.setVisibility(View.GONE);
            LL_list.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new RegistDayDetailListAdapter(items));
        }
    }

    private void createList() {
        if (!sessionCheck()) {
            netFail(R.string.regist_day_list_alert_title_fail, R.string.alert_content_not_login);
            return;
        }
        progressManager.actionWithState(new OnProgressAction() {
            @Override
            public void run() {
                Net.getInstance().getFactoryIm().selectDetailSchedule(scheduleModel.getTrip_no(), selected_date).enqueue(new Callback<ResponseArrayModel<ScheduleDayModel>>() {
                    @Override
                    public void onResponse(Call<ResponseArrayModel<ScheduleDayModel>> call, Response<ResponseArrayModel<ScheduleDayModel>> response) {
                        if (response.isSuccessful()) {
                            progressManager.endRunning();
                            ResponseArrayModel<ScheduleDayModel> result = response.body();
                            Log_HR.log(Log_HR.LOG_INFO, RegistDayList.class, "onResponse(Call<ResponseArrayModel<ScheduleDayModel>> call, Response<ResponseArrayModel<ScheduleDayModel>> response)", "body : " + result.getSuccess());
                            Log_HR.log(Log_HR.LOG_INFO, RegistDayList.class, "onResponse(Call<ResponseArrayModel<ScheduleDayModel>> call, Response<ResponseArrayModel<ScheduleDayModel>> response)", "body : " + result.getMessage());
                            Log_HR.log(Log_HR.LOG_INFO, RegistDayList.class, "onResponse(Call<ResponseArrayModel<ScheduleDayModel>> call, Response<ResponseArrayModel<ScheduleDayModel>> response)", "body : " + result.getResult());
                            switch (result.getSuccess()) {
                                case DataDefinition.Network.CODE_SUCCESS:
                                    HandlerManager.getInstance().getHandler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            items = result.getResult();
                                            if (items == null)
                                                items = new ArrayList<ScheduleDayModel>();
                                            registDayDetailListAdapter.notifyDataSetChanged();
                                            initLayout();
                                        }
                                    });
                                    break;
                                case DataDefinition.Network.CODE_ERROR:
                                    netFail(R.string.regist_day_list_alert_title_fail, R.string.regist_day_list_alert_content_fail);
                                    break;
                            }
                        } else
                            netFail(R.string.regist_day_list_alert_title_fail, R.string.regist_day_list_alert_content_fail);
                    }

                    @Override
                    public void onFailure(Call<ResponseArrayModel<ScheduleDayModel>> call, Throwable t) {
                        Log_HR.log(RegistDayList.class, "onFailure(Call<ResponseArrayModel<ScheduleDayModel>> call, Throwable t)", t);
                        netFail(R.string.regist_day_list_alert_title_fail, R.string.regist_day_list_alert_content_fail_2);
                    }
                });
            }
        });
    }

    private boolean sessionCheck() {
        userModel = SessionManager.getInstance().getUserModel();
        return SessionManager.getInstance().isLogin();
    }

    private void netFail(int title, int content) {
        initLayout();
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(RegistDayList.this, title, content);
    }

    private void onRegister() {
        intent.setClass(this, RegistDayDetail.class);
        startActivity(intent);
    }


}
