package com.android.project.util;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by macos on 05.10.16.
 */

class ImageReference {
    private ImageView mImageView;
    private ProgressBar mSpinner;
    private Animation mAnimation;

    public ImageReference(ImageView imageView) {
        mImageView = imageView;
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

    public void setSpinner(ProgressBar spinner) {
        mSpinner = spinner;
    }

    public void setAnimation(Animation animation) {
        mAnimation = animation;
    }
}
