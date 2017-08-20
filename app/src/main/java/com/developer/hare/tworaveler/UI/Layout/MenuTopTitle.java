package com.developer.hare.tworaveler.UI.Layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Parser.SizeManager;

/**
 * Created by Hare on 2017-08-02.
 */

public class MenuTopTitle extends LinearLayout {
    private final float Title_Size_SP = 25;
    private ImageButton IB_left, IB_right;
    private TextView TV_title;

    private UIFactory uiFactory;

    {
        init();
    }

    public MenuTopTitle(Context context) {
        super(context);
    }

    public MenuTopTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
    }

    public MenuTopTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        getAttrs(attrs, defStyle);
    }

    private void init() {
        uiFactory = UIFactory.getInstance(this);
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.menu_top_title, this, false);
        addView(v);

        IB_left = uiFactory.createView(R.id.menu_top_title$IB_left);
        IB_right = uiFactory.createView(R.id.menu_top_title$IB_right);
        TV_title = uiFactory.createView(R.id.menu_top_title$TV_title);
        FontManager.getInstance().setFont(TV_title, "SCRIPTBL.TTF");
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.menu_top_title);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.menu_top_title, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int leftId = typedArray.getResourceId(R.styleable.menu_top_title_leftButton, -1);
        if (leftId != -1)
            IB_left.setBackground(ResourcesCompat.getDrawable(getResources(), leftId, null));

        int rightId = typedArray.getResourceId(R.styleable.menu_top_title_rightButton, -1);
        if (rightId != -1)
            IB_right.setBackground(ResourcesCompat.getDrawable(getResources(), rightId, null));

        String title = typedArray.getString(R.styleable.menu_top_title_titleText);
        if (!title.isEmpty())
            TV_title.setText(title);
        float size = typedArray.getDimensionPixelSize(R.styleable.menu_top_title_titleTextSize, SizeManager.getInstance().convertSpToPixels(Title_Size_SP, getContext()));
        TV_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        typedArray.recycle();
    }

    public ImageButton getIB_left() {
        return IB_left;
    }

    public ImageButton getIB_right() {
        return IB_right;
    }

    public TextView getTV_title() {
        return TV_title;
    }
}
