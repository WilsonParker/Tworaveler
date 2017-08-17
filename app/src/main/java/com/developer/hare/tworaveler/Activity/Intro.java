package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.developer.hare.tworaveler.Data.ResourceManager;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.HandlerManager;

public class Intro extends AppCompatActivity {
    private Thread initThread = new Thread(new Runnable() {
        @Override
        public void run() {
            ResourceManager.getInstance().setResources(getResources());
            FontManager.getInstance().setAssetManager(getAssets());
            initComplete = true;
        }
    });
    private boolean initComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        init();
    }

    private void init() {
        initThread.start();

        HandlerManager.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                while (!initComplete) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startActivity(new Intent(Intro.this, Main.class));
                finish();
            }
        },500);
    }
}
