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

    public void action(OnProgressAction action) {
        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();
        checkThread = new Thread() {
            @Override
            public void run() {
                try {
                    //
                    /*while (!alertDialog.isShowing()) {
                        Thread.sleep(150);
                    }*/
                    runThread.start();
                    Thread.sleep(3000);
                    while (runThread.getState() == State.TERMINATED) {
                        Thread.sleep(150);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (alertDialog != null && alertDialog.isShowing())
                                    alertDialog.cancel();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        runThread = new Thread(new Runnable() {
            @Override
            public void run() {
                action.run();
            }
        });
        checkThread.start();
    }

}
