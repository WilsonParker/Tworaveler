package com.developer.hare.tworaveler.Data;

import android.content.res.Resources;

/**
 * Created by Hare on 2017-08-06.
 */

public class ResourceManager {
    private static final ResourceManager ourInstance = new ResourceManager();
    private Resources resources;

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public static ResourceManager getInstance() {
        return ourInstance;
    }

    public String getResourceString(int id) {
        return resources.getString(id);
    }
}
