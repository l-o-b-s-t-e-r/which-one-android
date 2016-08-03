package com.android.project.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lobster on 01.07.16.
 */
public class PictureLoaderImpl implements PictureLoader {

    private static final String FILE_PREFIX = "IMG_";
    private static final String FILE_SUFFIX = ".jpg";
    private File mPictureFile;
    private String mAlbumName;

    public PictureLoaderImpl(String albumName) {
        mAlbumName = albumName;
    }

    @Override
    public File createPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String imageFileName = FILE_PREFIX + timeStamp + "_";
        File album = getAlbumDir();
        File picture = File.createTempFile(imageFileName, FILE_SUFFIX, album);

        return picture;
    }

    @Override
    public File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mAlbumName);

            Log.i("DIR PATH", storageDir.toString());
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        }

        return storageDir;
    }

    @Override
    public File getPictureFile() {
        return mPictureFile;
    }

    @Override
    public Intent addPictureToGallery() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        Uri contentUri = Uri.fromFile(mPictureFile);
        mediaScanIntent.setData(contentUri);
        return mediaScanIntent;
    }

    @Override
    public Intent getChooserIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            mPictureFile = createPictureFile();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent pickUpPictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        final Intent chooserIntent = Intent.createChooser(pickUpPictureIntent, "Load image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

        return chooserIntent;
    }

}
