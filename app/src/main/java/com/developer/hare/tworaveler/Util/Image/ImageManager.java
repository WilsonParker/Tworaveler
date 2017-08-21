package com.developer.hare.tworaveler.Util.Image;

import android.content.Context;
import android.widget.ImageView;

import com.developer.hare.tworaveler.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by Hare on 2017-07-17.
 */

public class ImageManager {
    private static ImageManager imageManager = new ImageManager();
    public static final int BASIC_TYPE = 0x0000, FIT_TYPE = 0x0001, PICTURE_TYPE = 0x0010, THUMBNAIL_TYPE = 0x0011, ICON_TYPE = 0x0100;

    public static ImageManager getInstance() {
        return imageManager;
    }

    public void loadImage(Context context, String downloadURI, ImageView imageView, int type) {
        createRequestCreator(context, downloadURI, type).into(imageView);
    }

    public void loadImage(Context context, int id, ImageView imageView, int type) {
        createRequestCreator(context, id, type).into(imageView);
    }

    public void loadImage(Context context, File file, ImageView imageVIew, int type) {
        createRequestCreator(context, file, type).into(imageVIew);
    }

    public void loadImage(RequestCreator requestCreator, ImageView imageView) {
        requestCreator.into(imageView);
    }

    public RequestCreator createRequestCreator(Context context, int id, int type) {
        return requestCreatorSetCase(Picasso.with(context).load(id), type);
    }

    public RequestCreator createRequestCreator(Context context, String url, int type) {
        return requestCreatorSetCase(Picasso.with(context).load(url), type);
    }

    public RequestCreator createRequestCreator(Context context, File file, int type) {
        return requestCreatorSetCase(Picasso.with(context).load(file), type);
    }

    private RequestCreator basicSetting(RequestCreator requestCreator) {
        return requestCreator
                .error(R.drawable.noimage)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE);
    }

    private RequestCreator requestCreatorSetCase(RequestCreator requestCreator, int type) {
        requestCreator = basicSetting(requestCreator);
        switch (type) {
            case BASIC_TYPE:
                break;
            case FIT_TYPE:
                requestCreator.fit();
                break;
            case PICTURE_TYPE:
                requestCreator.resize(1280, 720);
                break;
            case THUMBNAIL_TYPE:
                requestCreator.resize(640, 360);
                break;
            case ICON_TYPE:
                requestCreator.resize(320, 180);
                break;
        }
        return requestCreator;
    }

}
