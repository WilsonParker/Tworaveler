package com.developer.hare.tworaveler.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.Util.HandlerManager;

/**
 * Created by Hare on 2017-08-10.
 */

public class ProgressManager {

    private Activity activity;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private View view;

    private Handler handler;
    private Thread checkThread, runThread;

    public ProgressManager(Activity activity) {
        this.activity = activity;
        initVal();
    }

    private void initVal() {
        view = LayoutInflater.from(activity).inflate(R.layout.progressbar_simple, null);
        progressBar = UIFactory.getInstance(activity).createView(R.id.progress_bar_simple$PB);

        handler = HandlerManager.getInstance().getHandler();
        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
    }

    public void alertShow() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void alertDismiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void action(OnProgressAction action) {
        runThread = new Thread(new Runnable() {
            @Override
            public void run() {
                action.run();
            }
        });
        checkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            alertShow();
                        }
                    });
//                    Thread.sleep(3000);
                    runThread.start();
                    while (!(runThread.getState() == Thread.State.TERMINATED)) {
                        Thread.sleep(150);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            alertDismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        checkThread.start();
    }

}
