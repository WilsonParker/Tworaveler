package com.developer.hare.tworaveler.Util;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Hare on 2017-08-14.
 */

public class FontManager {
    private AssetManager assetManager;
    private final String FontPath = "fonts/";
    private static FontManager fontManager = new FontManager();

    public static FontManager getInstance() {
        return fontManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void setFont(TextView textView, String font) {
        textView.setTypeface(Typeface.createFromAsset(assetManager, FontPath + font));
    }
}
