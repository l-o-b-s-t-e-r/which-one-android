package com.android.project.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestServiceImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Lobster on 16.09.16.
 */

public class ImageManager {


    public static final String IMAGE_URL = RequestServiceImpl.BASE_URL + RequestServiceImpl.IMAGE_FOLDER;
    public static final int LOAD_IMAGE = 1;
    public static final int LOAD_AVATAR = 2;
    public static final int SMALL_AVATAR_SIZE = 200;
    private static final String TAG = ImageManager.class.getSimpleName();
    private static final float CORNER_RADIUS = 50.0f;
    private static final Integer MAX_SIDE_LENGTH = 600;
    private static ImageManager mImageManager;

    private ImageManager() {

    }

    public static ImageManager getInstance() {
        if (mImageManager == null) {
            mImageManager = new ImageManager();
        }

        return mImageManager;
    }

    public String startLoadImage(final String imageName, int loadingCode) {

        if (loadingCode == LOAD_IMAGE) {
            Glide.with(WhichOneApp.getContext())
                    .load(IMAGE_URL + imageName)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        } else if (loadingCode == LOAD_AVATAR) {
            Glide.with(WhichOneApp.getContext())
                    .load(IMAGE_URL + imageName)
                    .downloadOnly(SMALL_AVATAR_SIZE, SMALL_AVATAR_SIZE);
        }

        return imageName;
    }


    public Target<Bitmap> createTarget(int width, int height, ImageView imageView) {
        return new SimpleTarget<Bitmap>(width, height) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                RoundedBitmapDrawable imageBitmapDrawable = RoundedBitmapDrawableFactory.create(WhichOneApp.getContext().getResources(), resource);
                imageBitmapDrawable.setCornerRadius(CORNER_RADIUS);

                imageView.setImageDrawable(imageBitmapDrawable);
            }
        };
    }


    public Observable<File> cropImageAsSquare(File imageFile) {
        return Observable.fromCallable(() -> {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            return ImageManager.this.cropImageAsSquare(bitmap, imageFile);
        }).subscribeOn(Schedulers.computation());
    }

    private File cropImageAsSquare(Bitmap bitmap, File imageFile) {
        if (bitmap.getWidth() != bitmap.getHeight()) {
            int sideLength = Math.min(bitmap.getHeight(), bitmap.getWidth());
            if (bitmap.getWidth() > bitmap.getHeight()) {
                bitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth() - sideLength) / 2, 0, sideLength, sideLength);
            } else {
                bitmap = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() - sideLength) / 2, sideLength, sideLength);
            }
        }

        Bitmap croppedBitmap = Bitmap.createScaledBitmap(bitmap, MAX_SIDE_LENGTH, MAX_SIDE_LENGTH, false);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        croppedBitmap.recycle();

        return imageFile;
    }

    public Observable<File> resizeImage(File imageFile) {
        return Observable.fromCallable(() -> {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            return ImageManager.this.resizeImage(bitmap, imageFile);
        }).subscribeOn(Schedulers.computation());
    }

    private File resizeImage(Bitmap bitmap, File imageFile) {
        Float scaleKoef;

        Log.i(TAG, String.format("bitmap: height - %d, width - %d, imageFile length: %d", bitmap.getHeight(), bitmap.getWidth(), imageFile.length()));

        if (bitmap.getHeight() <= MAX_SIDE_LENGTH && bitmap.getWidth() <= MAX_SIDE_LENGTH) {
            return imageFile;
        }

        if (bitmap.getHeight() >= bitmap.getWidth()) {
            scaleKoef = (float) MAX_SIDE_LENGTH / bitmap.getHeight();
        } else {
            scaleKoef = (float) MAX_SIDE_LENGTH / bitmap.getWidth();
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, Float.valueOf(scaleKoef * bitmap.getWidth()).intValue(), Float.valueOf(scaleKoef * bitmap.getHeight()).intValue(), false);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        resizedBitmap.recycle();

        Log.i(TAG, String.format("resizedBitmap: height - %d, width - %d, imageFile length: %d", resizedBitmap.getHeight(), resizedBitmap.getWidth(), imageFile.length()));
        return imageFile;
    }
}
