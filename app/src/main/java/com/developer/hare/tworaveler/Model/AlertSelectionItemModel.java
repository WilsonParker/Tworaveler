package com.developer.hare.tworaveler.Model;

import android.view.View;

/**
 * Created by Hare on 2017-08-06.
 */

public class AlertSelectionItemModel {
    private String text;
    private int imageId;
    private View.OnClickListener onClickListener;

    public AlertSelectionItemModel(String text, int imageId, View.OnClickListener onClickListener) {
        this.text = text;
        this.imageId = imageId;
        this.onClickListener = onClickListener;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
