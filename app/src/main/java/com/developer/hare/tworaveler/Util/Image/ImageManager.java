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
    private static ImageManager imageNanager = new ImageManager();

    public static ImageManager getInstance() {
        return imageNanager;
    }

    public void loadImage(Context context, String downloadURI, ImageView imageView) {
        requestCreator(Picasso.with(context).load(downloadURI)).into(imageView);
    }

    public void loadImage(Context context, int id, ImageView imageView) {
        requestCreator(Picasso.with(context).load(id)).into(imageView);
//            Log_HR.log(getClass(), "loadImage(Context, int, ImageView)", e);
    }

    public void loadImage(Context context, File file, ImageView imageVIew) {
        requestCreator(Picasso.with(context).load(file)).into(imageVIew);
    }

    private RequestCreator requestCreator(RequestCreator requestCreator) {
        return requestCreator.error(R.drawable.noimage).fit().centerCrop().
                memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE);
    }

}
