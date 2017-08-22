package com.developer.hare.tworaveler.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.AlertSelectionModeAdapter;
import com.developer.hare.tworaveler.Listener.OnInputAlertClickListener;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.ResourceManager;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Hare on 2017-08-02.
 */

public class AlertManager {
    private static final AlertManager ourInstance = new AlertManager();

    private AlertDialog alertDialog;
    private ResourceManager resourceManager;
    private Handler handler;

    private View view;
    private TextView TV_title;
    private Button BT_close;
    private RecyclerView RV_items;
    private ListView LV_items;

    {
        resourceManager = ResourceManager.getInstance();
    }

    public static AlertManager getInstance() {
        return ourInstance;
    }

    private SweetAlertDialog setAlert(Context context, int alertType) {
        return new SweetAlertDialog(context, alertType);
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content) {
        SweetAlertDialog dialog = createAlert(context, alertType, title, content, "확인");
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content, String confirm) {
        SweetAlertDialog dialog = setAlert(context, alertType).setTitleText(title).setContentText(content).setConfirmText(confirm);
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content, String confirm, String cancel) {
        SweetAlertDialog dialog = setAlert(context, alertType).setContentText(content).setConfirmText(confirm).setTitleText(title).setCancelText(cancel);
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content, SweetAlertDialog.OnSweetClickListener confirmClickListener) {
        SweetAlertDialog dialog = createAlert(context, alertType, title, content, "확인").setConfirmClickListener(confirmClickListener);
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content, String confirm, SweetAlertDialog.OnSweetClickListener confirmClickListener) {
        SweetAlertDialog dialog = createAlert(context, alertType, title, content, confirm).setConfirmClickListener(confirmClickListener);
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content, String confirm, SweetAlertDialog.OnSweetClickListener confirmClick, String cancel, SweetAlertDialog.OnSweetClickListener cancelClick) {
        SweetAlertDialog dialog = createAlert(context, alertType, title, content, confirm, confirmClick).setCancelText(cancel).setCancelClickListener(cancelClick);
        return dialog;
    }

    public AlertDialog showAlertSelectionMode(Activity activity, String title, int spanCount, ArrayList<AlertSelectionItemModel> items) {
        handler = HandlerManager.getInstance().getHandler();
        view = LayoutInflater.from(activity).inflate(R.layout.alert_selectionmode, null);
        UIFactory uiFactory = UIFactory.getInstance(view);
        TV_title = uiFactory.createView(R.id.alert_selectionmode$TV_title);
        TV_title.setText(title);
        RV_items = uiFactory.createView(R.id.alert_selectionmode$RV_items);
        RV_items.setLayoutManager(new GridLayoutManager(activity, spanCount, LinearLayoutManager.VERTICAL, false));
        RV_items.setAdapter(new AlertSelectionModeAdapter(items));
        BT_close = uiFactory.createView(R.id.alert_selectionmode$BT_close);
        BT_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAlertSelectionMode();
            }
        });
        alertDialog = new AlertDialog.Builder(activity).setView(view).setCancelable(false).create();
        alertDialog.show();
        return alertDialog;
    }

    public void dismissAlertSelectionMode() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public SweetAlertDialog showLoading(Context context) {
        return showLoading(context, "Loading");
    }

    public SweetAlertDialog showLoading(Context context, String msg) {
        return showLoading(context, msg, "#A5DC86");
    }

    public SweetAlertDialog showLoading(Context context, String msg, String color) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false); // 백키를 눌러도 닫히지 않는다.
        pDialog.show();

        return pDialog;
    }

    public void showPopup(Context context, String title, String msg,
                          String cName, SweetAlertDialog.OnSweetClickListener cEvent,
                          String oName, SweetAlertDialog.OnSweetClickListener oEvent) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(msg)
                .setConfirmText(cName)
                .setConfirmClickListener(cEvent)
                .setCancelText(oName)
                .setCancelClickListener(oEvent)
                .show();
    }

    public void showSimplePopup(Context context, String title, String msg, int type) {
        new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(msg)
                .show();
    }

    public void showNetFailAlert(Activity activity, int title, int content) {
        createAlert(activity, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((title)), resourceManager.getResourceString((content))).show();
    }

   /* public void showInputAlert(Activity activity, int title, int message, OnInputAlertClickListener onConfirmClickListener) {
        AlertDialog.Builder ad = new AlertDialog.Builder(activity);
        ad.setTitle(resourceManager.getResourceString(title));       // 제목 설정
        ad.setMessage(resourceManager.getResourceString(message));   // 내용 설정

        // EditText 삽입하기
        final EditText et = new EditText(activity);
        ad.setView(et);

        // 확인 버튼 설정
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onConfirmClickListener.onConfirmClick(et.getText().toString());
            }
        });

        // 취소 버튼 설정
        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });

        ad.show();
    }*/

    public void showInputAlert(Activity activity, int title, int message, OnInputAlertClickListener onConfirmClickListener) {
        View view = activity.getLayoutInflater().inflate(R.layout.alert_input_check, null);
        UIFactory uiFactory = UIFactory.getInstance(view);
        AlertDialog dialog = new AlertDialog.Builder(activity).setView(view).setCancelable(false).create();
        dialog.setView(view);
        ((TextView) uiFactory.createView(R.id.alert_input_check$TV_title)).setText(resourceManager.getResourceString(title));
        ((TextView) uiFactory.createView(R.id.alert_input_check$TV_content)).setText(resourceManager.getResourceString(message));
        EditText ET_input = uiFactory.createView(R.id.alert_input_check$ET_input);
        uiFactory.createView(R.id.alert_input_check$TV_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirmClickListener.onConfirmClick(ET_input.getText().toString());
                dialog.dismiss();
            }
        });
        uiFactory.createView(R.id.alert_input_check$TV_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
//        ad.show();
    }
}
