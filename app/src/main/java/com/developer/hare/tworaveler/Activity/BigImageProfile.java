package com.developer.hare.tworaveler.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Exception.NullChecker;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import uk.co.senab.photoview.PhotoView;

public class BigImageProfile extends Activity {

    private RecyclerView recyclerView;
    private UIFactory uiFactory;
    private PhotoView photoView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_profile);
        init();

    }

    private void init() {
        UserModel model = (UserModel) getIntent().getExtras().get(DataDefinition.Intent.KEY_USERMODEL);
        uiFactory = UIFactory.getInstance(this);
        photoView = uiFactory.createView(R.id.item_profile$PV_image);
        imageView = uiFactory.createView(R.id.item_profile$IV_image);
        ImageManager imageManager = ImageManager.getInstance();
        if(NullChecker.getInstance().nullCheck(model.getProfile_pic_url())){
            photoView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageManager.loadImage(imageManager.createRequestCreator(this, R.drawable.image_profile, ImageManager.FIT_TYPE), imageView);
        }else{
            photoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            imageManager.loadImage(imageManager.createRequestCreator(this, model.getProfile_pic_url(), ImageManager.FIT_TYPE), photoView);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
