package com.android.project.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Lobster on 14.09.16.
 */

public class ImageEntity {

    public static final String RECORD_ID_FIELD_NAME = "record_id";
    public static final String IMAGE_NAME_FIELD_NAME = "image_name";

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = RECORD_ID_FIELD_NAME, index = true)
    private RecordEntity mRecord;
    @DatabaseField(id = true, columnName = IMAGE_NAME_FIELD_NAME)
    private String mImagePath;

    public ImageEntity() {

    }

    public ImageEntity(RecordEntity record, String imagePath) {
        mRecord = record;
        mImagePath = imagePath;
    }

    public RecordEntity getRecord() {
        return mRecord;
    }

    public void setRecord(RecordEntity record) {
        mRecord = record;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "mRecord=" + mRecord +
                ", mImagePath='" + mImagePath + '\'' +
                '}';
    }
}
