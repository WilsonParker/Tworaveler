package com.developer.hare.tworaveler.Util;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hare on 2017-08-14.
 */

public class FontManager {
    private AssetManager assetManager;
    private final String FontPath = "fonts/";
    private final String[] strFonts = {"NotoSansCJKkr-Bold.otf", "NotoSansCJKkr-Medium.otf","SCRIPTBL.TTF","NotoSansCJKkr-Regular.otf"
                                        ,"Roboto-Medium.ttf"};
    private static FontManager fontManager = new FontManager();
    private static Map<String, Typeface> fonts = new HashMap<>();

    public static FontManager getInstance() {
        return fontManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        initFonts();
    }

    public void setFont(TextView textView, String font) {
        textView.setTypeface(fonts.get(font));
    }

    public void setFont(ArrayList<TextView> items, String font) {
        for (TextView textView : items)
            setFont(textView, font);
    }

    private void initFonts() {
        for (String font : strFonts)
            fonts.put(font, Typeface.createFromAsset(assetManager, FontPath + font));
    }
}
