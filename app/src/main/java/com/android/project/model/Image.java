package com.android.project.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import java.util.List;

/**
 * Created by Lobster on 31.05.16.
 */

public class Image{

    private Long recordId;
    private String image;
    private Bitmap bitmapImage;

    public Image(Long recordId, String image) {
        this.recordId = recordId;
        this.image = image;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }
}
