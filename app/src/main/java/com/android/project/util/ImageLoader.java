package com.android.project.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Lobster on 16.09.16.
 */

public class ImageLoader {

    private static ImageLoader mImageLoader;

    private final String IMAGE_URL = RequestServiceImpl.BASE_URL + RequestServiceImpl.IMAGE_FOLDER;
    private Map<String, List<ImageReference>> mImageReferences;

    private ImageLoader() {
        mImageReferences = new HashMap<>();
    }

    public static ImageLoader getInstance() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader();
        }

        return mImageLoader;
    }

    public void addImageReference(String imagePath, ImageReference imageReference) {
        List<ImageReference> imageReferences;

        if ((imageReferences = mImageReferences.get(imagePath)) == null) {
            mImageReferences.put(imagePath, new ArrayList<>(Collections.singletonList(imageReference)));
        } else {
            imageReferences.add(imageReference);
        }
    }


    public String savePictureToInternalStorage(final String imageName, final Context context) {
        final File imageFile = new File(context.getFilesDir(), imageName);

        createBitmapObservable(IMAGE_URL + imageName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createBitmapAction(imageFile, context));

        return imageFile.getAbsolutePath();
    }

    private Observable<Bitmap> createBitmapObservable(String imagePath) {
        return Observable.just(
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(imagePath)
        );
    }

    private Action1<Bitmap> createBitmapAction(final File imageFile, final Context context) {
        return new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                List<ImageReference> imageReferences;
                FileOutputStream outputStream = null;

                try {
                    outputStream = context.openFileOutput(imageFile.getName(), Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                if ((imageReferences = mImageReferences.get(imageFile.getAbsolutePath())) != null) {
                    completeImageReference(imageFile.getAbsolutePath(), bitmap, imageReferences);
                }
            }
        };
    }

    private void completeImageReference(String imagePath, Bitmap bitmap, List<ImageReference> imageReferences) {
        for (ImageReference imageReference : imageReferences) {
            imageReference.setBitmap(bitmap);
        }

        mImageReferences.remove(imagePath);
    }
}
