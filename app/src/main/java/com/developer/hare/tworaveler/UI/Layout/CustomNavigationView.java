package com.developer.hare.tworaveler.UI.Layout;

import android.content.Context;
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

public class CustomNavigationView extends ViewGroup {
    private View view;
    private ArrayList<NavigationItemView> children;
    private NavigationItemView clickedItem;

    public CustomNavigationView(Context context) {
        super(context);
    }

    public CustomNavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    public void bindItemView(Context context, ArrayList<NavigationItem> items) {
        children = new ArrayList<>();
        view = LayoutInflater.from(context).inflate(R.layout.custom_navigation_view, null);
        UIFactory uiFactory = UIFactory.getInstance(view);
        LinearLayout linearLayout = uiFactory.createView(R.id.custom_navigation_view$LL);
        for (NavigationItem item : items) {
            NavigationItemView itemVIew = new NavigationItemView(context, this);
            children.add(itemVIew);
            linearLayout.addView(itemVIew.toBind(item));
        }
        clickedItem = children.get(0);
        clickedItem.clickEvent();
    }


    public class NavigationItemView {
        private View view;
        private ImageView icon;
        private Context context;

        private NavigationItem item;

        public NavigationItemView(Context context, ViewGroup viewGroup) {
            this.context = context;
            view = LayoutInflater.from(context).inflate(R.layout.item_custom_navigation_view, viewGroup, false);
            icon = UIFactory.getInstance(view).createView(R.id.item_custom_navigation_view$IV);
        }

        public View toBind(NavigationItem item) {
            this.item = item;
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.getOnClickListener().onClick();
                    clickEvent();
                }
            });
            return view;
        }

        public void clickEvent() {
            item.setClicked(!item.isClicked());
            checkState(item);
        }

        private void checkState(NavigationItem item) {
            if (item.isClicked())
                ImageManager.getInstance().loadImage(context, item.getClickImage(), icon);
            else
                ImageManager.getInstance().loadImage(context, item.getDefaultImage(), icon);
        }
    }

    public class NavigationItem {
        private int clickImage, defaultImage;
        private NavigationOnClickListener onClickListener;
        private boolean isClicked;

        public NavigationItem(int clickImage, int defaultImage, NavigationOnClickListener onClickListener) {
            this.clickImage = clickImage;
            this.defaultImage = defaultImage;
            this.onClickListener = onClickListener;
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
    }

    public interface NavigationOnClickListener {
        void onClick();
    }
}
