package com.developer.hare.tworaveler.Fragment.Page;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.hare.tworaveler.Activity.SearchCity;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentRegistDayDetail extends BaseFragment {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private String strDate;
    private DateManager dateManager;
    private Bundle bundle;

    private com.prolificinteractive.materialcalendarview.MaterialCalendarView meterialCalendarView;
    private TextView TV_citySearch, TV_dateStart, TV_dateEnd;
    private ImageView IV_cover;
//    private CalendarView calendar;

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_regist$TV_start:
                    dateManager.getDateTime(getActivity(), TV_dateStart);
                    break;
                case R.id.fragment_regist$TV_end:
                    dateManager.getDateTime(getActivity(), TV_dateEnd);
                    break;
                case R.id.fragment_regist$TV_citySearch:
                    Intent intent = new Intent(getActivity(), SearchCity.class);
                    startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
                    break;
                case R.id.fragment_regist$IV_cover:
                    ArrayList<AlertSelectionItemModel> AlertSelectionItemModels = new ArrayList<>();
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.button_left, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onCameraSelect(getActivity(), new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    ImageManager.getInstance().loadImage(getActivity(), fileData.getFile(), IV_cover);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                }
                            });
                        }
                    }));
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("갤러리", R.drawable.button_left, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onGallerySingleSelect(getActivity(), new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    ImageManager.getInstance().loadImage(getActivity(), fileData.getFile(), IV_cover);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                }
                            });
                        }
                    }));
                    AlertManager.getInstance().showAlertSelectionMode(getActivity(), "등록 방법 선택", 3, AlertSelectionItemModels).show();
                    break;
            }
        }
    };


    public static FragmentRegistDayDetail newInstance(String date) {
        FragmentRegistDayDetail f = new FragmentRegistDayDetail();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString(DataDefinition.Bundle.KEY_STARTDATE, date);
        f.setArguments(args);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regist_day_detail_list, null);
    }

    @Override
    protected void init(View view) {
        bundle = getArguments();
        strDate = bundle.getString(DataDefinition.Bundle.KEY_DATE);
        Log_HR.log(Log_HR.LOG_INFO, FragmentRegistDayDetail.class, "init(View)", "strDate " + strDate);

        uiFactory = UIFactory.getInstance(view);
        dateManager = DateManager.getInstance();

        menuTopTitle = uiFactory.createView(R.id.fragment_regist$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegist();
            }
        });
        createListDate();
    }

    private void createListDate(){

    }

    private void onRegist() {

    }
}