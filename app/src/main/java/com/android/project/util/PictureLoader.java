package com.android.project.util;

import android.content.Intent;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lobster on 01.07.16.
 */
public interface PictureLoader {

    File createPictureFile() throws IOException;

    File getAlbumDir();

    File getPictureFile();

    Intent addPictureToGallery();

    Intent getChooserIntent();

}
