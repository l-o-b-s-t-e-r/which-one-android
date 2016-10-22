package com.android.project.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;

import com.android.project.api.RequestServiceImpl;
import com.nostra13.universalimageloader.core.ImageLoader;

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

public class ImageManager {

    private static ImageManager mImageManager;

    private final String IMAGE_URL = RequestServiceImpl.BASE_URL + RequestServiceImpl.IMAGE_FOLDER;
    private Map<String, List<Subscriber<Bitmap>>> mImageSubscribers;

    private ImageManager() {
        mImageSubscribers = new HashMap<>();
    }

    public static ImageManager getInstance() {
        if (mImageManager == null) {
            mImageManager = new ImageManager();
        }

        return mImageManager;
    }

    public static File cropImageAsSquare(File imageFile) {
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
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(createBitmapAction(imageFile, context));

        return imageFile.getAbsolutePath();
    }

    private Observable<Bitmap> createBitmapObservable(final String imagePath) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable
                        .just(ImageLoader.getInstance().loadImageSync(imagePath));
            }
        });
    }

    private Action1<Bitmap> createBitmapAction(final File imageFile, final Context context) {
        return new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                Log.i("ImageManager", "createBitmapAction - UI Thread: " + String.valueOf(Looper.getMainLooper() == Looper.myLooper()));
                List<Subscriber<Bitmap>> imageSubscribers;
                FileOutputStream outputStream = null;

                try {
                    outputStream = context.openFileOutput(imageFile.getName(), Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

                if ((imageSubscribers = mImageSubscribers.get(imageFile.getAbsolutePath())) != null) {
                    notifyAllSubscribers(imageFile.getAbsolutePath(), Observable.just(bitmap), imageSubscribers);
                }
            }
        };
    }

    private void notifyAllSubscribers(String imagePath, Observable<Bitmap> observable, List<Subscriber<Bitmap>> subscribers) {
        for (Subscriber<Bitmap> subscriber : subscribers) {
            Log.i("ImageManager", "ShowImage: " + String.valueOf(Looper.getMainLooper() == Looper.myLooper()));

            observable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

        mImageSubscribers.remove(imagePath);
    }
}
