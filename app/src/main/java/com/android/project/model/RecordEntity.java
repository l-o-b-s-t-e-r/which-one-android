package com.android.project.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by Lobster on 14.09.16.
 */

public class RecordEntity {

    public static final String RECORD_ID_FIELD_NAME = "record_id";
    public static final String USER_NAME_FIELD_NAME = "user_name";
    public static final String AVATAR_PATH_FIELD_NAME = "avatar_path";
    public static final String TITLE_FIELD_NAME = "title";
    public static final String IMAGES_FIELD_NAME = "images";

    @DatabaseField(id = true, columnName = RECORD_ID_FIELD_NAME)
    private Long mRecordId;
    @DatabaseField(columnName = USER_NAME_FIELD_NAME)
    private String mUsername;
    @DatabaseField(columnName = AVATAR_PATH_FIELD_NAME)
    private String mAvatarPath;
    @DatabaseField(columnName = TITLE_FIELD_NAME)
    private String mTitle;
    @ForeignCollectionField(columnName = IMAGES_FIELD_NAME, eager = true)
    private ForeignCollection<ImageEntity> mImages;

    public RecordEntity() {

    }

    public RecordEntity(Long recordId, String username, String avatarPath, String title, ForeignCollection<ImageEntity> images) {
        mRecordId = recordId;
        mUsername = username;
        mAvatarPath = avatarPath;
        mTitle = title;
        mImages = images;
    }

    public Long getRecordId() {
        return mRecordId;
    }

    public void setRecordId(Long recordId) {
        mRecordId = recordId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getAvatarPath() {
        return mAvatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        mAvatarPath = avatarPath;
    }

    public ForeignCollection<ImageEntity> getImages() {
        return mImages;
    }

    public void setImages(ForeignCollection<ImageEntity> images) {
        mImages = images;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String toString() {
        return "RecordEntity{" +
                "mTitle='" + mTitle + '\'' +
                ", mAvatarPath='" + mAvatarPath + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mRecordId=" + mRecordId +
                '}';
    }
}
