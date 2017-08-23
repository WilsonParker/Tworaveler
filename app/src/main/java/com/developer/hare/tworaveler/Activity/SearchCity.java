package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.developer.hare.tworaveler.Adapter.CityListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Listener.OnSelectCityListener;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
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

public class SearchCity extends AppCompatActivity {
    private OnSelectCityListener onSelectCityListener = new OnSelectCityListener() {
        @Override
        public void onSelectCity(CityModel model) {
            onFinish(DataDefinition.Intent.RESULT_CODE_SUCCESS, model);
        }
    };

    private UIFactory uiFactory;
    private CityListAdapter cityListAdapter;
    private ArrayList<CityModel> items = new ArrayList<>();

    //    private MenuTopTitle menuTopTitle;
    private RecyclerView RV_list;
    private EditText ET_city;
    private ImageButton IB_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        init();
    }

    private void init() {
        uiFactory = UIFactory.getInstance(this);
        RV_list = uiFactory.createView(R.id.search_city$RV_list);
        RV_list.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        cityListAdapter = new CityListAdapter(onSelectCityListener, items, getBaseContext());
        RV_list.setAdapter(cityListAdapter);
        ET_city = uiFactory.createView(R.id.search_city$ET_city);
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
                Log_HR.log(Log_HR.LOG_INFO, SearchCity.class, "afterTextChanged(Editable)", "running");
                Net.getInstance().getFactoryIm().searchCity(ET_city.getText().toString()).enqueue(new Callback<ResponseArrayModel<CityModel>>() {
                    @Override
                    public void onResponse(Call<ResponseArrayModel<CityModel>> call, Response<ResponseArrayModel<CityModel>> response) {
                        if (response.isSuccessful()) {
                            ResponseArrayModel<CityModel> result = response.body();
                            items = result.getResult();
                            /*cityListAdapter = new CityListAdapter(onSelectCityListener, result.getResult(), getBaseContext());
                            RV_list.setAdapter(cityListAdapter);*/
//                            Log_HR.log(Log_HR.LOG_INFO, SearchCity.class, "afterTextChanged(Editable)", "itemSize" + items.size());
                            HandlerManager.getInstance().getHandler().post(new Runnable() {
                                @Override
                                public void run() {
//                                    cityListAdapter.notifyDataSetChanged();
                                    cityListAdapter = new CityListAdapter(onSelectCityListener, items, getBaseContext());
                                    RV_list.setAdapter(cityListAdapter);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseArrayModel<CityModel>> call, Throwable t) {

                    }
                });

            }
        });
        IB_search = uiFactory.createView(R.id.search_city$IB_cancel);
        IB_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ET_city.setText("");
                onFinish(DataDefinition.Intent.RESULT_CODE_SUCCESS, null);
            }
        });
    }

    private void onFinish(int code, CityModel model) {
        Intent intent = new Intent();
        if (code == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
            intent.putExtra(DataDefinition.Intent.KEY_CITYMODEL, model);
        }
        setResult(code, intent);
        finish();
    }
}
