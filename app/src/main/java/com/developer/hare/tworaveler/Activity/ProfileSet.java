package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.Model.ProfileModel;
import com.developer.hare.tworaveler.Model.Request.RequestArrayModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.Net.NetFactoryIm;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSet extends AppCompatActivity {

    private EditText editText;
    private CircleImageView circleImageView;
    private MenuTopTitle menuTopTitle;
    private UIFactory uiFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);
        init();
    }

    private void init(){
        uiFactory = UIFactory.getInstance(this);

        editText        = uiFactory.createView(R.id.profile_set$ET_nickname);
        editText        = uiFactory.createView(R.id.profile_set$ET_message);
        menuTopTitle    = uiFactory.createView(R.id.profile_set$topbar);
        circleImageView = uiFactory.createView(R.id.profile_setIV_profile);

        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<AlertSelectionItemModel> AlertSelectionItemModels = new ArrayList<>();
                AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.ic_camera_alt_black_24dp, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhotoManager.getInstance().onCameraSelect(ProfileSet.this, new OnPhotoBindListener() {
                            @Override
                            public void bindData(FileData fileData) {
                                ImageManager.getInstance().loadImage(ProfileSet.this, fileData.getFile(), circleImageView);
                                AlertManager.getInstance().dismissAlertSelectionMode();
                            }
                        });
                    }
                }));
                AlertSelectionItemModels.add(new AlertSelectionItemModel("갤러리", R.drawable.ic_filter_black_24dp, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhotoManager.getInstance().onGallerySingleSelect(ProfileSet.this, new OnPhotoBindListener() {
                            @Override
                            public void bindData(FileData fileData) {
                                ImageManager.getInstance().loadImage(ProfileSet.this, fileData.getFile(), circleImageView);
                                AlertManager.getInstance().dismissAlertSelectionMode();
                            }
                        });
                    }

                }));
                AlertManager.getInstance().showAlertSelectionMode(ProfileSet.this, "등록 방법 선택", 2, AlertSelectionItemModels).show();

            }
        });
    }
    public void onLogout(View view){
        AlertManager.getInstance().showPopup(ProfileSet.this,
                "알림",
                "로그아웃 하시겠습니까?",
                "취소",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                },
                "확인",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(ProfileSet.this, SignIn.class));
                    }
                }
        );
    }
    public void onWithdrawal(View view){
        AlertManager.getInstance().showPopup(ProfileSet.this,
                "알림",
                "정말이야? 다시 한번 생각해봐 ㅠㅠ",
                "취소",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                },
                "확인",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(ProfileSet.this, SignIn.class));
                    }
                }
        );
    }

    private void bind(FileData fileData)
    {
        final SweetAlertDialog sweetAlertDialog = AlertManager.getInstance().showLoading(this);
        NetFactoryIm im = Net.getInstance().getFactoryIm();
        Call<RequestArrayModel<ProfileModel>> res = im.getProfile();
        res.enqueue(new Callback<RequestArrayModel<ProfileModel>>() {
            @Override
            public void onResponse(Call<RequestArrayModel<ProfileModel>> call, Response<RequestArrayModel<ProfileModel>> response) {

                sweetAlertDialog.dismissWithAnimation();
                AlertManager.getInstance().showSimplePopup(ProfileSet.this, "알림",
                        "사진 업로드가 성공하였습니다.", SweetAlertDialog.ERROR_TYPE);
            }

            @Override
            public void onFailure(Call<RequestArrayModel<ProfileModel>> call, Throwable t) {
                Log.i("TAG", "업로드 실패 : " + t.getMessage());
                sweetAlertDialog.dismissWithAnimation();
                AlertManager.getInstance().showSimplePopup(ProfileSet.this, "알림",
                        "사진 업로드가 실패하였습니다. 잠시 후 다시 이용해주세요.", SweetAlertDialog.ERROR_TYPE);
            }
        });
    }

}
