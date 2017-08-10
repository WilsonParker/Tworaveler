package com.developer.hare.tworaveler.Fragment.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.SearchCity;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Fragment.Page.FragmentRegistDetail;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;

import java.util.ArrayList;

import static com.developer.hare.tworaveler.R.id.fragment_regist$TV_start;

public class FragmentRegist extends BaseFragment {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private DateManager dateManager;

    private TextView TV_citySearch, TV_dateStart, TV_dateEnd;
    private ImageView IV_cover;

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
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.ic_camera_alt_black_24dp, new View.OnClickListener() {
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
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("갤러리", R.drawable.ic_filter_black_24dp, new View.OnClickListener() {
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
                    AlertManager.getInstance().showAlertSelectionMode(getActivity(), "등록 방법 선택", 3, AlertSelectionItemModels);
                    break;
            }
        }
    };

    public FragmentRegist() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regist, null);
    }

    @Override
    protected void init(View view) {
        uiFactory = UIFactory.getInstance(view);
        dateManager = DateManager.getInstance();

        menuTopTitle = uiFactory.createView(R.id.fragment_regist$menuToptitle);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegist();
            }
        });
        TV_citySearch = uiFactory.createView(R.id.fragment_regist$TV_citySearch);
        TV_citySearch.setOnClickListener(onClickListener);
        TV_dateStart = uiFactory.createView(fragment_regist$TV_start);
        TV_dateStart.setOnClickListener(onClickListener);
        TV_dateEnd = uiFactory.createView(R.id.fragment_regist$TV_end);
        TV_dateEnd.setOnClickListener(onClickListener);
        IV_cover = uiFactory.createView(R.id.fragment_regist$IV_cover);
        IV_cover.setOnClickListener(onClickListener);
    }

    private void onRegist() {
        getFragmentManager().beginTransaction().replace(R.id.main$FL_content, new FragmentRegistDetail(TV_dateStart.getText().toString(), TV_dateEnd.getText().toString())).addToBackStack(null).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == DataDefinition.Intent.RESULT_CODE_SEARCH_CITY) {
            // Make sure the request was successful
            if (resultCode == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
                CityModel model = (CityModel) data.getSerializableExtra(DataDefinition.Intent.KEY_CITYMODEL);
                TV_citySearch.setText(model.getCityName());
            }
        }
    }
}