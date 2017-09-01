package com.developer.hare.tworaveler.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;

public class BigImageProfile extends Activity {

    private RecyclerView recyclerView;
    private UIFactory uiFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_photoview);
        init();

    }

    private void init() {
        uiFactory = UIFactory.getInstance(this);
        UserModel models = (UserModel) getIntent().getExtras().get(DataDefinition.Intent.KEY_USERMODEL);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
    }
}
