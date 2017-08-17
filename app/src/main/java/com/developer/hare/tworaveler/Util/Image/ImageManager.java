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

    public static ImageManager getInstance() {
        return imageManager;
    }

    public void loadImage(Context context, String downloadURI, ImageView imageView) {
        createRequestCreator(context, downloadURI).into(imageView);
    }

    public void loadImage(Context context, int id, ImageView imageView) {
        createRequestCreator(context, id).into(imageView);
    }

    public void loadImage(Context context, File file, ImageView imageVIew) {
        createRequestCreator(context, file).into(imageVIew);
    }

    public void loadImage(RequestCreator requestCreator,ImageView imageView) {
        requestCreator.into(imageView);
    }

    public RequestCreator createRequestCreator(Context context, int id) {
        return basicSetting(Picasso.with(context).load(id));
    }

    public RequestCreator createRequestCreator(Context context, String url) {
        return basicSetting(Picasso.with(context).load(url));
    }

    public RequestCreator createRequestCreator(Context context, File file) {
        return basicSetting(Picasso.with(context).load(file));
    }

    private RequestCreator basicSetting(RequestCreator requestCreator) {
        return requestCreator
                .error(R.drawable.noimage).fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE);
    }

}
