package com.developer.hare.tworaveler.Fragment.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.hare.tworaveler.Activity.SearchCity;
import com.developer.hare.tworaveler.Adapter.PeedListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.DummyDataFactory;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Log_HR;

public class FragmentPeed extends BaseFragment {
    private static FragmentPeed instance = new FragmentPeed();
    private PeedListAdapter peedListAdapter;
    private RecyclerView recyclerView;
    private MenuTopTitle menuTopTitle;
    private UIFactory uiFactory;


    public FragmentPeed() {
    }


    public static FragmentPeed newInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peed, container, false);
        return view;
    }

    @Override
    protected void init(View view) {
        Log_HR.log(Log_HR.LOG_INFO, getClass(), "init()", "init running");
        uiFactory = UIFactory.getInstance(view);
        menuTopTitle = uiFactory.createView(R.id.fragment_peed$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchCity.class);
                startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
            }
        });

//        uiFactory = UIFactory.getInstance(view);
        recyclerView = uiFactory.createView(R.id.fragment_peed$RV);
        peedListAdapter = new PeedListAdapter(DummyDataFactory.createPeedItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(peedListAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == DataDefinition.Intent.RESULT_CODE_SEARCH_CITY) {
            // Make sure the request was successful
            if (resultCode == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
                if (data != null) {
                    CityModel model = (CityModel) data.getSerializableExtra(DataDefinition.Intent.KEY_CITYMODEL);
                }
            }
        }
    }
}
