package com.developer.hare.tworaveler.Fragment.Page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.BigImageProfile;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentFeed;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Exception.NullChecker;
import com.developer.hare.tworaveler.UI.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.LogManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Bundle.KEY_USER_NO;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_USERMODEL;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_NONE_SESSION;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_NOT_LOGIN;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class FragmentFeedProfile extends BaseFragment {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private TextView TV_cntFollower, TV_cntFollowing, TV_nickname, TV_message;
    private ImageView IV_profile;
    private int user_no;
    private UserModel model;

    public static FragmentFeedProfile newInstance(int user_no) {
        FragmentFeedProfile fragment = new FragmentFeedProfile();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_USER_NO, user_no);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_page_profile, null);
    }

    @Override
    protected void init(View view) {
        user_no = getArguments().getInt(KEY_USER_NO);

        uiFactory = UIFactory.getInstance(getActivity());
        menuTopTitle = uiFactory.createView(R.id.fragment_mypage_profile$topbar);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager.getInstance().setFragmentContent(FragmentFeed.newInstance());
            }
        });
        menuTopTitle.getIB_right().setVisibility(View.INVISIBLE);
        IV_profile = uiFactory.createView(R.id.fragment_mypage_profile$IV_profile);
        IV_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BigImageProfile.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(KEY_USERMODEL, model);
                getActivity().startActivity(intent);
//                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.center_zoom_in);
//                IV_profile.startAnimation(animation);

            }
        });
        TV_cntFollower = uiFactory.createView(R.id.fragment_mypage_profile$TV_cntfollower);
        TV_cntFollowing = uiFactory.createView(R.id.fragment_mypage_profile$TV_cntfollowing);
        TV_nickname = uiFactory.createView(R.id.fragment_mypage_profile$TV_nickname);
        TV_message = uiFactory.createView(R.id.fragment_mypage_profile$TV_message);
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(TV_cntFollower);
        textViews.add(TV_cntFollowing);
        textViews.add(TV_nickname);
        textViews.add(TV_message);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Medium.otf");
        textViews.clear();
        textViews.add(TV_cntFollower);
        textViews.add(TV_cntFollowing);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Bold.otf");
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        Net.getInstance().getFactoryIm().selectUserInfo(user_no).enqueue(
                new Callback<ResponseModel<UserModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                        ResponseModel<UserModel> result = response.body();
                        LogManager.log(FragmentMyPageProfile.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                        if (response.isSuccessful()) {
                            switch (result.getSuccess()) {
                                case CODE_SUCCESS:
                                    model = result.getResult();
                                    TV_cntFollower.setText(model.getFollowers().size() + "");
                                    TV_cntFollowing.setText(model.getFollowees().size() + "");
                                    TV_nickname.setText(model.getNickname());
                                    TV_message.setText(model.getStatus_message());
                                     ImageManager imageManager = ImageManager.getInstance();
                                    if(NullChecker.getInstance().nullCheck(model.getProfile_pic_url())){
                                         imageManager.loadImage(imageManager.createRequestCreator(getActivity(), R.drawable.image_profile, ImageManager.BASIC_TYPE), IV_profile);
                                    }else{
                                         imageManager.loadImage(imageManager.createRequestCreator(getActivity(), model.getProfile_pic_thumbnail_url(), ImageManager.THUMBNAIL_TYPE).placeholder(R.drawable.image_profile), IV_profile);
                                    }
                                    break;
                                case CODE_NOT_LOGIN:
                                    break;
                                case CODE_NONE_SESSION:
                                    break;
                            }
                        } else{
                            AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.profileSet_info_fail_alert_title, R.string.profileSet_info_fail_alert_content);
                            LogManager.log(LogManager.LOG_INFO, FragmentFeedProfile.class, "onResponse", "onResponse is not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                        AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.profileSet_info_fail_alert_title, R.string.profileSet_info_fail_alert_content);
                        LogManager.log(FragmentFeedProfile.class, "onFailure(Call<ResponseArrayModel<CommentModel>> call, Throwable t)", t);
                    }
                }
        );
    }
}