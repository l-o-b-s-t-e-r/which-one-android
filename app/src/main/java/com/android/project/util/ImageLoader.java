package com.android.project.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lobster on 16.09.16.
 */

public class ImageLoader {

    private static ImageLoader mImageLoader;

    private final String IMAGE_URL = RequestServiceImpl.BASE_URL + RequestServiceImpl.IMAGE_FOLDER;
    private Map<String, List<ImageRef>> mImageRefs;

    private ImageLoader() {
        mImageRefs = new HashMap<>();
    }

    public static ImageLoader getInstance() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader();
        }

        return mImageLoader;
    }

    public void pushImage(String imagePath, ImageView imageView, ProgressBar spinner, Animation animation) {
        List<ImageRef> imageRefs;
        if ((imageRefs = mImageRefs.get(imagePath)) == null) {
            mImageRefs.put(imagePath, new ArrayList<>(Collections.singletonList(new ImageRef(imageView, spinner, animation))));
        } else {
            imageRefs.add(new ImageRef(imageView, spinner, animation));
        }
    }

    public String savePictureToInternalStorage(String imageName, Context context) {
        File imageFile = new File(context.getFilesDir(), imageName);
        try {
            FileOutputStream outputStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            new LoadImageAsyncTask(imageFile.getAbsolutePath(), outputStream).execute(IMAGE_URL + imageName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return imageFile.getAbsolutePath();
    }


    private class ImageRef {
        private ImageView mImageView;
        private ProgressBar mSpinner;
        private Animation mAnimation;

        public ImageRef(ImageView imageView, ProgressBar spinner, Animation animation) {
            mImageView = imageView;
            mSpinner = spinner;
            mAnimation = animation;
        }

        public void setBitmap(Bitmap bitmapImage) {
            mImageView.setImageBitmap(bitmapImage);

            if (mSpinner != null) {
                mSpinner.setVisibility(View.GONE);
            }

            if (mAnimation != null) {
                mImageView.startAnimation(mAnimation);
            }
        }
    }

    private class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private String mImagePath;
        private FileOutputStream mOutputStream;

        public LoadImageAsyncTask(String imagePath, FileOutputStream outputStream) {
            mImagePath = imagePath;
            mOutputStream = outputStream;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, mOutputStream);

            List<ImageRef> imageRefs;
            if ((imageRefs = mImageRefs.get(mImagePath)) != null) {
                for (ImageRef imageRef : imageRefs) {
                    imageRef.setBitmap(bitmap);
                }
                mImageRefs.remove(mImagePath);
            }
        }
    }

}
