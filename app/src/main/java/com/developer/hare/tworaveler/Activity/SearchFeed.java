package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.CityListAdapter;
import com.developer.hare.tworaveler.Adapter.FeedCityListAdapter;
import com.developer.hare.tworaveler.Adapter.FeedNicknameListAdapter;
import com.developer.hare.tworaveler.Adapter.NicknameListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Listener.OnSelectCityListener;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.color.black;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_CITYMODEL;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.RESULT_CODE_CITY_MODEL;

public class SearchFeed extends AppCompatActivity {

    private UIFactory uiFactory;
    private FeedCityListAdapter cityListAdapter;
    private FeedNicknameListAdapter nicknameListAdapter;
    private ArrayList<CityModel> cityItems = new ArrayList<>();
    private ScheduleModel nicknameItems;

    //    private MenuTopTitle menuTopTitle;
    private RecyclerView RV_citylist, RV_nicknamelist;
    private EditText ET_city;
    private TextView city, nickname;
    private ImageButton IB_search;
    private boolean isCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_feed);
        init();
    }

    private void init() {
        uiFactory = UIFactory.getInstance(this);
        city = uiFactory.createView(R.id.search_feed$city);
        nickname = uiFactory.createView(R.id.search_feed$nickname);
        RV_citylist = uiFactory.createView(R.id.search_feed$RV_citylist);
        RV_citylist.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));


        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCity = true;
                selectFeed(isCity);
            }
        });
        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCity = false;
                selectFeed(isCity);
            }
        });

        ET_city = uiFactory.createView(R.id.search_feed$ET_city);
        FontManager.getInstance().setFont(ET_city, "NotoSansCJKkr-Medium.otf");
        ET_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(isCity) {
                    Log_HR.log(Log_HR.LOG_INFO, SearchFeed.class, "afterTextChanged(Editable)", "running city");
                    Net.getInstance().getFactoryIm().searchCity( ET_city.getText().toString()).enqueue(new Callback<ResponseArrayModel<CityModel>>() {
                        @Override
                        public void onResponse(Call<ResponseArrayModel<CityModel>> call, Response<ResponseArrayModel<CityModel>> response) {
                            if (response.isSuccessful()) {
                                ResponseArrayModel<CityModel> result = response.body();
                                cityItems = result.getResult();
                                HandlerManager.getInstance().getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
//                                    cityListAdapter.notifyDataSetChanged();
//                                        RV_citylist.setAdapter(new FeedCityListAdapter(items, getApplicationContext()));
                                        RV_citylist.setAdapter(new CityListAdapter(new OnSelectCityListener() {
                                            @Override
                                            public void onSelectCity(CityModel model) {
                                                Intent intent = new Intent();
                                                intent.putExtra(KEY_CITYMODEL, model);
                                                setResult(RESULT_CODE_CITY_MODEL, intent);
                                                finish();
                                            }
                                        }, cityItems, getApplicationContext() ));
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseArrayModel<CityModel>> call, Throwable t) {
                            Log_HR.log(Log_HR.LOG_INFO, SearchFeed.class, "onResponse(Call<ResponseArrayModel<ScheduleDayModel>> call, Response<ResponseArrayModel<ScheduleDayModel>> response)", "onFail : " + t);
                        }
                    });
                }else{
                    Log_HR.log(Log_HR.LOG_INFO, SearchFeed.class, "afterTextChanged(Editable)", "running nickname");
                    Net.getInstance().getFactoryIm().searchNickname(ET_city.getText().toString()).enqueue(new Callback<ResponseModel<String>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                            /*if (response.isSuccessful()) {
                                Log_HR.log(Log_HR.LOG_INFO, SearchFeed.class, "afterTextChanged(Editable)", "running nickname"+ response.body());
                                ResponseModel<String> result = response.body();
                                nicknameItems = result.getResult();
                                HandlerManager.getInstance().getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
//                                    cityListAdapter.notifyDataSetChanged();
                                        RV_citylist.setAdapter(new NicknameListAdapter(new OnSelectNicknameListener() {
                                            @Override
                                            public void onSelectNickname(ScheduleModel model) {
                                                Intent intent = new Intent();
                                                intent.putExtra(KEY_SCHEDULE_MODEL, "");
                                                setResult(RESULT_CODE_SCHEDULE_MODEL, intent);
                                                finish();
                                            }
                                        }, nicknameItems, getApplicationContext()));
                                    }
                                });
                            }*/
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                            Log_HR.log(Log_HR.LOG_INFO, SearchFeed.class, "onResponse(Call<ResponseArrayModel<ScheduleDayModel>> call, Response<ResponseArrayModel<ScheduleDayModel>> response)", "onFail : " + t);
                        }
                    });
                }
            }
        });
        IB_search = uiFactory.createView(R.id.search_feed$IB_cancel);
        IB_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ET_city.setText("");
                onFinish(DataDefinition.Intent.RESULT_CODE_SUCCESS, null);
            }
        });
        city.callOnClick();
    }

    private void onFinish(int code, ScheduleModel model) {
        Intent intent = new Intent();
        if (code == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
            intent.putExtra(KEY_CITYMODEL, model);
        }
        setResult(code, intent);
        finish();
    }

    private void selectFeed(boolean isCity){
        if(isCity){
            city.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.menu_top_title_color));
            nickname.setTextColor(ContextCompat.getColor(getBaseContext(), black));
            RV_citylist.setAdapter(new CityListAdapter(null, cityItems, getApplicationContext() ));
            ET_city.setHint("찾으시는 도시를 검색하세요");
        }else {
            nickname.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.menu_top_title_color));
            city.setTextColor(ContextCompat.getColor(getBaseContext(), black));
            RV_citylist.setAdapter(new NicknameListAdapter(null, nicknameItems, getApplicationContext()));
            ET_city.setHint("찾으시는 닉네임의 풀네임을 적어주세요");
        }
    }
}
