package com.developer.hare.tworaveler.Util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by Hare on 2017-08-16.
 */

public class BackClickManager {
    private Activity activity;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_FIRST_CLICK:
                    if (isClicked)
                        activity.finish();
                    handler.sendEmptyMessageDelayed(MESSAGE_RESET_CLICK, RE_CLICK_TIMEOUT);
                    isClicked = true;
                    Toast.makeText(activity, "한번 더 누르시면 종료 합니다", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_RESET_CLICK:
                    isClicked = false;
                    break;
            }
        }
    };

    private boolean isClicked;
    private final int
            MESSAGE_FIRST_CLICK = 0x0001, MESSAGE_RESET_CLICK = 0x0010, RE_CLICK_TIMEOUT = 1500;

    private static BackClickManager backClickManager = new BackClickManager();

    public static BackClickManager getInstance() {
        return backClickManager;
    }

    public void onBackPressed(Activity activity) {
        this.activity = activity;
        handler.sendEmptyMessage(MESSAGE_FIRST_CLICK);
    }
}
