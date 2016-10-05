package com.android.project.util;

import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by macos on 05.10.16.
 */
public class ImageReferenceBuilder {

    private ImageReference mImageReference;

    private ImageReferenceBuilder(ImageView imageView) {
        mImageReference = new ImageReference(imageView);
    }

    public static ImageReferenceBuilder builder(ImageView imageView) {
        return new ImageReferenceBuilder(imageView);
    }

    public ImageReferenceBuilder spinner(ProgressBar spinner) {
        mImageReference.setSpinner(spinner);
        return this;
    }

    public ImageReferenceBuilder animation(Animation animation) {
        mImageReference.setAnimation(animation);
        return this;
    }

    public ImageReference build() {
        return mImageReference;
    }
}
