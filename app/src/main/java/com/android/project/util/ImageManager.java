package com.android.project.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestServiceImpl;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Lobster on 16.09.16.
 */

public class ImageManager {

    public static final String IMAGE_URL = RequestServiceImpl.BASE_URL + RequestServiceImpl.IMAGE_FOLDER;
    private static final Float CORNER_RADIUS = 50.0f;

    private static ImageManager mImageManager;

    private ImageManager() {

    }

    public static ImageManager getInstance() {
        if (mImageManager == null) {
            mImageManager = new ImageManager();
        }

        return mImageManager;
    }

    public String startLoadImage(String imageName) {
        WhichOneApp.getPicasso()
                .load(IMAGE_URL + imageName)
                .fetch();

        return imageName;
    }

    public Target createTarget(final ImageView imageView) {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                RoundedBitmapDrawable imageBitmapDrawable = RoundedBitmapDrawableFactory.create(WhichOneApp.getContext().getResources(), bitmap);
                imageBitmapDrawable.setCornerRadius(CORNER_RADIUS);

                imageView.setImageDrawable(imageBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

    public File cropImageAsSquare(File imageFile) {
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        if (bitmap.getWidth() == bitmap.getHeight()) {
            return imageFile;
        }

        int sideLength = Math.min(bitmap.getHeight(), bitmap.getWidth());
        if (bitmap.getWidth() > bitmap.getHeight()) {
            bitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth() - sideLength) / 2, 0, sideLength, sideLength);
        } else {
            bitmap = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() - sideLength) / 2, sideLength, sideLength);
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        return imageFile;
    }
}
