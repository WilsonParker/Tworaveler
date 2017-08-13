package com.developer.hare.tworaveler.UI;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.R;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.yalantis.ucrop.UCrop;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hare on 2017-08-03.
 */

public class PhotoManager {
    private static final PhotoManager ourInstance = new PhotoManager();
    private OnPhotoBindListener onPhotoBindListener;
    private UCrop.Options options = new UCrop.Options();

    public static PhotoManager getInstance() {
        return ourInstance;
    }

    private void createOptions(Activity activity) {
        options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
    }

    public void onGalleryMultiSelect(Activity activity, OnPhotoBindListener onPhotoBindListener) {
        this.onPhotoBindListener = onPhotoBindListener;
        createOptions(activity);

        RxPaparazzo.multiple(activity)
                .usingGallery()  //  바로 앨범을 실행
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.resultCode() != RESULT_OK) {
                        return;
                    }
                    response.data().forEach(this::bindData);
                });
    }

    public void onGallerySingleSelect(Activity activity, OnPhotoBindListener onPhotoBindListener) {
        this.onPhotoBindListener = onPhotoBindListener;
        createOptions(activity);

        RxPaparazzo.single(activity)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.resultCode() != RESULT_OK) {
                        return;
                    }
                    bindData(response.data());
                });
    }

    public void onCameraSelect(Activity activity, OnPhotoBindListener onPhotoBindListener) {
        this.onPhotoBindListener = onPhotoBindListener;
        createOptions(activity);

        RxPaparazzo.single(activity)
                .crop(options)
                .usingCamera()  //  바로 카메라를 실행
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // See response.resultCode() doc
                    if (response.resultCode() != RESULT_OK) {
                        return;
                    }
                    bindData(response.data());
                });
    }

    private void bindData(FileData fileData) {
        onPhotoBindListener.bindData(fileData);
    }
}