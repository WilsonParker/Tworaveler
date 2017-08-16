package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.developer.hare.tworaveler.Data.ResourceManager;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.Util.FontManager;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        init();
    }

    private void init() {
        ResourceManager.getInstance().setResources(getResources());
        FontManager.getInstance().setAssetManager(getAssets());

        startActivity(new Intent(getBaseContext(), Main.class));
        finish();
    }
}
