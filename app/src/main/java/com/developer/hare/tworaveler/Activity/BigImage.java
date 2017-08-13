package com.developer.hare.tworaveler.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.developer.hare.tworaveler.Adapter.PhotoViewAdaper;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class BigImage extends Activity {

    private PhotoView photoView;
    private RecyclerView recyclerView;
    private UIFactory uiFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        init();

    }

    private void init() {
        uiFactory = UIFactory.getInstance(this);
        ArrayList<BagModel> models = (ArrayList<BagModel>) getIntent().getExtras().get(DataDefinition.Intent.KEY_BAGMODELS);
//        BagModel model = (BagModel) getIntent().getExtras().get(DataDefinition.Intent.KEY_BAGMODEL);
        int position = (int) getIntent().getExtras().get(DataDefinition.Intent.KEY_POSITION);

        recyclerView = uiFactory.createView(R.id.activity_big_image$RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new PhotoViewAdaper(models));
        Log_HR.log(Log_HR.LOG_INFO, getClass(),"init()","index : "+position);
        recyclerView.scrollToPosition(position);
//        recyclerView.setScrollX(models.indexOf(model));


        /*photoView = uiFactory.createView(R.id.PV_image);
        BagModel model = (BagModel) getIntent().getExtras().get(DataDefinition.Intent.KEY_BAGMODEL);
        if (model.isFile())
            ImageManager.getInstance().loadImage(getBaseContext(), model.getFile(), photoView);
        else
            ImageManager.getInstance().loadImage(getBaseContext(), model.getImage(), photoView);*/
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
    }
}
