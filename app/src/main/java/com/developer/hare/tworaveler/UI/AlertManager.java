package com.developer.hare.tworaveler.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.ItemAdatperTest;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Hare on 2017-08-02.
 */

public class AlertManager {
    private static final AlertManager ourInstance = new AlertManager();

    private AlertDialog alertDialog;
    private Handler handler;

    private View view;
    private TextView TV_title;
    private Button BT_close;
    private RecyclerView RV_items;
    private ListView LV_items;

    public static AlertManager getInstance() {
        return ourInstance;
    }

    private SweetAlertDialog setAlert(Context context, int alertType) {
        return new SweetAlertDialog(context, alertType);
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content, String confirm, String cancel) {
        SweetAlertDialog dialog = setAlert(context, alertType).setContentText(content).setConfirmText(confirm).setTitleText(title).setCancelText(cancel);
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content, String confirm, SweetAlertDialog.OnSweetClickListener confirmClick, String cancel, SweetAlertDialog.OnSweetClickListener cancelClick) {
        SweetAlertDialog dialog = createAlert(context, alertType, title, content, confirm, confirmClick).setCancelText(cancel).setCancelClickListener(cancelClick);
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content, String confirm) {
        SweetAlertDialog dialog = setAlert(context, alertType).setTitleText(title).setContentText(content).setConfirmText(confirm);
        return dialog;
    }

    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content, String confirm, SweetAlertDialog.OnSweetClickListener clickListener) {
        SweetAlertDialog dialog = createAlert(context, alertType, title, content, confirm).setConfirmClickListener(clickListener);
        return dialog;
    }


    public SweetAlertDialog createAlert(Context context, int alertType, String title, String content) {
        SweetAlertDialog dialog = createAlert(context, alertType, title, content, "확인");
        return dialog;
    }

    public AlertDialog showAlertSelectionMode(Activity activity, String title, int spanCount, ArrayList<AlertSelectionItemModel> items) {
        handler = HandlerManager.getHandler();
        view = LayoutInflater.from(activity).inflate(R.layout.alert_selectionmode, null);
        UIFactory uiFactory = UIFactory.getInstance(view);
        TV_title = uiFactory.createView(R.id.alert_selectionmode$TV_title);
        TV_title.setText(title);
//        RV_items = uiFactory.createView(R.id.alert_selectionmode$RV_items);
//        RV_items.setLayoutManager(new GridLayoutManager(activity, spanCount, LinearLayoutManager.VERTICAL, false));
//        RV_items.setAdapter(new AlertSelectionModeAdapter(items));
        LV_items = uiFactory.createView(R.id.alert_selectionmode$RV_items);
        LV_items.setAdapter(new ItemAdatperTest(items, activity));
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

    private View setItem(Context context, AlertSelectionItemModel model) {
        ImageView IV_icon;
        TextView TV_text;
        View view = LayoutInflater.from(context).inflate(R.layout.item_alert_selectionmode, null);
        UIFactory uiFactory = UIFactory.getInstance(view);
        IV_icon = uiFactory.createView(R.id.item_alert_selectionmode$IV_icon);
        TV_text = uiFactory.createView(R.id.item_alert_selectionmode$TV_text);
        ImageManager.getInstance().loadImage(context, model.getImageId(), IV_icon);
        IV_icon.setOnClickListener(model.getOnClickListener());
        TV_text.setText(model.getText());

        return view;
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

}
