package com.developer.hare.tworaveler.Fragment.Menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;

public class FragmentAlarm extends BaseFragment {
    private static FragmentAlarm instance = new FragmentAlarm();
    private TextView textView;
    private UIFactory uiFactory;

    public FragmentAlarm() {
        // Required empty public constructor
    }

    public static FragmentAlarm newInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, null);
    }

    @Override
    protected void init(View view) {
        uiFactory = UIFactory.getInstance(view);
        textView = uiFactory.createView(R.id.fragment_alarm$TV_alarm);
        FontManager.getInstance().setFont(textView,  "NotoSansCJKkr-Regular.otf");
    }
}
