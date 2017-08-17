package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.ResourceManager;

public class Intro extends AppCompatActivity {
    private Thread initThread = new Thread(new Runnable() {
        @Override
        public void run() {
            ResourceManager.getInstance().setResources(getResources());
            FontManager.getInstance().setAssetManager(getAssets());
            initComplete = true;

//            USER_MODEL = new UserModel(0,"m_user_email","m_user_pw","m_user_nickname","","","","","");
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
        HandlerManager.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initThread.start();
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
