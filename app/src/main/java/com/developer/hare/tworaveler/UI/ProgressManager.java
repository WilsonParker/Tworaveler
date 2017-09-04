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

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Hare on 2017-08-10.
 */

public class ProgressManager {

    private Activity activity;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private SweetAlertDialog sweetAlertDialog;
    private View view;

    private Handler handler;
    private Thread checkThread, runThread, stateThread;
    private boolean state = false;
    private boolean isDialog;

    public ProgressManager(Activity activity) {
        this.activity = activity;
//        initCustom();
        initDialog();
    }

    private void initCustom() {
        isDialog = false;
        view = LayoutInflater.from(activity).inflate(R.layout.progressbar_simple, null);
        progressBar = UIFactory.getInstance(activity).createView(R.id.progress_bar_simple$PB);

        handler = HandlerManager.getInstance().getHandler();
        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
    }

    private void initDialog() {
        isDialog = true;
        handler = HandlerManager.getInstance().getHandler();
        sweetAlertDialog = AlertManager.getInstance().createLoadingDialog(activity);
    }

    public void alertShow() {
        if (isDialog) {
            if (sweetAlertDialog != null && !sweetAlertDialog.isShowing())
                sweetAlertDialog.show();
        } else {
            if (alertDialog != null && !alertDialog.isShowing())
                alertDialog.show();
        }
    }

    public void alertDismiss() {
        if (isDialog) {
            if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                sweetAlertDialog.dismiss();
        } else {
            if (alertDialog != null && alertDialog.isShowing())
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

    public void actionWithState(OnProgressAction action) {
        state = false;
        stateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            alertShow();
                        }
                    });
                    action.run();
                    while (!state) {
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
        stateThread.start();
    }

    public void endRunning() {
        this.state = true;
    }
}
