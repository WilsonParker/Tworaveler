package com.developer.hare.tworaveler.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FontManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.File.FileManager;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Parser.SizeManager;
import com.developer.hare.tworaveler.Util.ResourceManager;

public class Intro extends AppCompatActivity {
    private Activity activity;
    private Thread initThread = new Thread(new Runnable() {
        @Override
        public void run() {
            ResourceManager.getInstance().setResources(getResources());
            FontManager.getInstance().setAssetManager(getAssets());
            FileManager.getInstance().setActivity(activity);
            SizeManager.init(Intro.this);
            UIFactory.init(Intro.this);
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
        activity = this;
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
                Intent intent = new Intent(Intro.this, Main.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        }, 500);
    }
}
