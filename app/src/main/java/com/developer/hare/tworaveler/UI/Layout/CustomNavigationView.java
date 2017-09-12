package com.developer.hare.tworaveler.UI.Layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Image.ImageManager;

import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-02.
 */

public class CustomNavigationView extends LinearLayout {
    private View view;
    private ArrayList<NavigationItemView> children;
    private NavigationItemView clickedItemView;
    private UIFactory uiFactory;

    private int itemId;

    public CustomNavigationView(Context context) {
        super(context);
    }

    public CustomNavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
    }

    public CustomNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs, defStyleAttr);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.custom_navigation_view);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.custom_navigation_view, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        itemId = typedArray.getResourceId(R.styleable.custom_navigation_view_item_layout, R.layout.item_custom_navigation_view);
        typedArray.recycle();
    }


    public View bindItemView(Context context, ArrayList<NavigationItem> items) {
        children = new ArrayList<>();
        uiFactory = UIFactory.getInstance(this);

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        view = li.inflate(R.layout.custom_navigation_view, this, false);
        addView(view);

        LinearLayout linearLayout = uiFactory.createView(R.id.custom_navigation_view$LL);
        for (NavigationItem item : items) {
            NavigationItemView itemVIew = new NavigationItemView(context, this);
            children.add(itemVIew);
            linearLayout.addView(itemVIew.toBind(item));
        }
        return view;
    }

    public void actionItem(int position) {
        children.get(position).actionClickEvent();
    }

    public void setItemSelect(int position) {
        NavigationItemView navigationItemView= children.get(position);
        navigationItemView.setState(true);
        clickedItemView.setState(false);
        clickedItemView = navigationItemView;

    }

    public class NavigationItemView {
        private View view;
        private ImageView icon;
        private Context context;

        private NavigationItem item;

        public NavigationItemView(Context context, ViewGroup viewGroup) {
            this.context = context;
            view = LayoutInflater.from(context).inflate(itemId, viewGroup, false);
            icon = UIFactory.getInstance(view).createView(R.id.item_custom_navigation_view$IV);
        }

        public View toBind(NavigationItem item) {
            this.item = item;
            ImageManager.getInstance().loadImage(context, item.getDefaultImage(), icon, ImageManager.FIT_TYPE);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionClickEvent();
                }
            });
            return view;
        }

        private void actionClickEvent() {
            if (!item.isClicked()) {
                item.getOnClickListener().onClick();
                if (item.isChangable()) {
                    setState(true);
                    if (clickedItemView != null)
                        clickedItemView.setState(false);
                    clickedItemView = this;
                }
            }
        }

        private void setState(boolean clicked) {
            item.setClicked(clicked);
            ImageManager imageManager = ImageManager.getInstance();
            if (clicked)
                imageManager.loadImage(imageManager.createRequestCreator(context, item.getClickImage(), ImageManager.FIT_TYPE).placeholder(item.getDefaultImage()), icon);
            else
                imageManager.loadImage(imageManager.createRequestCreator(context, item.getDefaultImage(), ImageManager.FIT_TYPE).placeholder(item.getDefaultImage()), icon);
        }
    }

    public class NavigationItem {
        private int clickImage, defaultImage;
        private NavigationOnClickListener onClickListener;
        private boolean isClicked, changable = true;

        public NavigationItem(int clickImage, int defaultImage, NavigationOnClickListener onClickListener) {
            this.clickImage = clickImage;
            this.defaultImage = defaultImage;
            this.onClickListener = onClickListener;
        }

        public NavigationItem(int clickImage, int defaultImage, NavigationOnClickListener onClickListener, boolean changable) {
            this.clickImage = clickImage;
            this.defaultImage = defaultImage;
            this.onClickListener = onClickListener;
            this.changable = changable;
        }

        public int getClickImage() {
            return clickImage;
        }

        public void setClickImage(int clickImage) {
            this.clickImage = clickImage;
        }

        public int getDefaultImage() {
            return defaultImage;
        }

        public void setDefaultImage(int defaultImage) {
            this.defaultImage = defaultImage;
        }

        public NavigationOnClickListener getOnClickListener() {
            return onClickListener;
        }

        public void setOnClickListener(NavigationOnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public boolean isClicked() {
            return isClicked;
        }

        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }

        public boolean isChangable() {
            return changable;
        }

        public void setChangable(boolean changable) {
            this.changable = changable;
        }
    }

    public interface NavigationOnClickListener {
        void onClick();
    }
}
