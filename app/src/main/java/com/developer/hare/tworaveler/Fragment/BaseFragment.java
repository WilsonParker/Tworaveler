package com.developer.hare.tworaveler.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.developer.hare.tworaveler.Listener.OnBackClickListener;

/**
 * Created by Hare on 2017-08-01.
 */

public abstract class BaseFragment extends Fragment implements OnBackClickListener {

    @Override
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    protected void finishFragment() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    abstract protected void init(View view);
}
