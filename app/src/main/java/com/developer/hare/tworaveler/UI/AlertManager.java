package com.developer.hare.tworaveler.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import com.developer.hare.tworaveler.Activity.SignIn;
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

    private final String DEFAULT_CONFIRM_TEXT = "확인", DEFAULT_CANCEL_TEXT = "취소";

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

    public SweetAlertDialog createAlert(Context context, int alertType, int title, int content) {
        SweetAlertDialog dialog = createAlert(context, alertType, resourceManager.getResourceString(title), resourceManager.getResourceString(content), "확인");
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, int title, int content, int confirm) {
        SweetAlertDialog dialog = setAlert(context, alertType).setTitleText(resourceManager.getResourceString(title)).setContentText(resourceManager.getResourceString(content)).setConfirmText(resourceManager.getResourceString(confirm));
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, int title, int content, int confirm, int cancel) {
        SweetAlertDialog dialog = setAlert(context, alertType).setContentText(resourceManager.getResourceString(content)).setConfirmText(resourceManager.getResourceString(confirm)).setTitleText(resourceManager.getResourceString(title)).setCancelText(resourceManager.getResourceString(cancel));
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, int title, int content, SweetAlertDialog.OnSweetClickListener confirmClickListener) {
        SweetAlertDialog dialog = createAlert(context, alertType, resourceManager.getResourceString(title), resourceManager.getResourceString(content), "확인").setConfirmClickListener(confirmClickListener);
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, int title, int content, int confirm, SweetAlertDialog.OnSweetClickListener confirmClickListener) {
        SweetAlertDialog dialog = createAlert(context, alertType, title, content, confirm).setConfirmClickListener(confirmClickListener);
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, int title, int content, int confirm, SweetAlertDialog.OnSweetClickListener confirmClick, int cancel, SweetAlertDialog.OnSweetClickListener cancelClick) {
        SweetAlertDialog dialog = createAlert(context, alertType, title, content, confirm, confirmClick).setCancelText(resourceManager.getResourceString(cancel)).setCancelClickListener(cancelClick);
        return dialog;
    }

    public void showNoTitleAlert(Context context, int alertType, int content, SweetAlertDialog.OnSweetClickListener confirmClick) {
        setAlert(context, alertType).setTitleText(resourceManager.getResourceString(content)).setConfirmText(DEFAULT_CONFIRM_TEXT).setConfirmClickListener(confirmClick).setCancelText(DEFAULT_CANCEL_TEXT).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        }).show();
    }
    public SweetAlertDialog createNoTitleAlert(Context context, int alertType, int content, int confirm, SweetAlertDialog.OnSweetClickListener confirmClick, int cancel, SweetAlertDialog.OnSweetClickListener cancelClick) {
        SweetAlertDialog dialog = setAlert(context, alertType).setContentText(resourceManager.getResourceString(content)).setCancelText(resourceManager.getResourceString(confirm)).setConfirmClickListener(confirmClick).setCancelText(resourceManager.getResourceString(cancel)).setCancelClickListener(cancelClick);
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
    public void showPopup(Context context, String msg,
                          String cName, SweetAlertDialog.OnSweetClickListener cEvent,
                          String oName, SweetAlertDialog.OnSweetClickListener oEvent) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
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

    public SweetAlertDialog createLoadingDialog(Context context) {
        return createLoadingDialog(context, resourceManager.getResourceString(R.string.progress_message));
    }

    public SweetAlertDialog createLoadingDialog(Context context, String msg) {
        return createLoadingDialog(context, msg, resourceManager.getResourceString(R.color.default_theme_Image_color));
    }

    public SweetAlertDialog createLoadingDialog(Context context, String msg, String color) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(color));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(false); // 백키를 눌러도 닫히지 않는다.
        return pDialog;
    }

    public SweetAlertDialog createLoadingDialog(Context context, int msg, int color) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(resourceManager.getResourceString(color)));
        pDialog.setTitleText(resourceManager.getResourceString(msg));
        pDialog.setCancelable(false);
        return pDialog;
    }

    public void showNetFailAlert(Activity activity, int title, int content) {
        showNetFailAlert((Context) activity, title, content);
    }

    public void showNetFailAlert(Context context, int title, int content) {
        createAlert(context, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((title)), resourceManager.getResourceString((content))).show();
    }

    public void showNotLoginAlert(Context context, int title) {
        createAlert(context, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((title)), resourceManager.getResourceString((R.string.alert_content_not_login)), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                context.startActivity(new Intent(context, SignIn.class));
            }
        }).show();
    }

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
    }

}
