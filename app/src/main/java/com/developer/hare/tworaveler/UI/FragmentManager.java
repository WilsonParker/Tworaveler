package com.developer.hare.tworaveler.UI;

import android.app.Activity;
import android.app.Fragment;

import com.developer.hare.tworaveler.R;

/**
 * Created by Hare on 2017-08-07.
 */

public class FragmentManager {
    private static final FragmentManager ourInstance = new FragmentManager();
    private Activity activity;

    public static FragmentManager getInstance() {
        return ourInstance;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setFragmentContent(Fragment fragment) {
        activity.getFragmentManager().beginTransaction().replace(R.id.main$FL_content, fragment).addToBackStack(null).commit();

    }
}
