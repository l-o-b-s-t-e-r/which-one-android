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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by Lobster on 16.09.16.
 */

public class ImageLoader {

    private static ImageLoader mImageLoader;

    private final String IMAGE_URL = RequestServiceImpl.BASE_URL + RequestServiceImpl.IMAGE_FOLDER;
    private Map<String, List<Subscriber<Bitmap>>> mImageSubscribers;

    private ImageLoader() {
        mImageSubscribers = new HashMap<>();
    }

    public static ImageLoader getInstance() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader();
        }

        return mImageLoader;
    }

    public void addImageSubscriber(String imagePath, Subscriber<Bitmap> subscriber) {
        List<Subscriber<Bitmap>> imageSubscribers;

        if ((imageSubscribers = mImageSubscribers.get(imagePath)) == null) {
            mImageSubscribers.put(imagePath, new ArrayList<>(Collections.singletonList(subscriber)));
        } else {
            imageSubscribers.add(subscriber);
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

    private Observable<Bitmap> createBitmapObservable(final String imagePath) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable.just(com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(imagePath));
            }
        });
    }

    private Action1<Bitmap> createBitmapAction(final File imageFile, final Context context) {
        return new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                List<Subscriber<Bitmap>> imageSubscribers;
                FileOutputStream outputStream = null;

                try {
                    outputStream = context.openFileOutput(imageFile.getName(), Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                if ((imageSubscribers = mImageSubscribers.get(imageFile.getAbsolutePath())) != null) {
                    notifyAllSubscribers(imageFile.getAbsolutePath(), Observable.just(bitmap), imageSubscribers);
                }
            }
        };
    }


    private void notifyAllSubscribers(String imagePath, Observable<Bitmap> observable, List<Subscriber<Bitmap>> subscribers) {
        for (Subscriber<Bitmap> subscriber : subscribers) {
            observable.subscribe(subscriber);
        }

        mImageSubscribers.remove(imagePath);
    }
}
