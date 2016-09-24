package com.android.project.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lobster on 01.07.16.
 */
public class ImageKeeper {

    private static ImageKeeper mImageKeeper;

    private File mImageFile;

    private ImageKeeper() {

    }

    public static ImageKeeper getInstance() {
        if (mImageKeeper == null) {
            mImageKeeper = new ImageKeeper();
        }

        return mImageKeeper;
    }

    public Intent getChooserIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mImageFile = createFile(context);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));

        Intent pickUpPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(pickUpPictureIntent, "Load image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

        return chooserIntent;
    }

    private File createFile(Context context) {
        return new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault()).format(new Date())
        );
    }

    public File getImageFile() {
        return mImageFile;
    }
}
