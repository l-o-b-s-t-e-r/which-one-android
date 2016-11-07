package com.android.project.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Lobster on 06.11.16.
 */
public class UserEntity {

    public static final String USERNAME_FIELD_NAME = "username";
    public static final String PASSWORD_FILED_NAME = "password";
    public static final String AVATAR_FIELD_NAME = "avatar";
    public static final String BACKGROUND_FILED_NAME = "background";

    @DatabaseField(id = true, columnName = USERNAME_FIELD_NAME)
    private String mUsername;
    @DatabaseField(columnName = PASSWORD_FILED_NAME)
    private String mPassword;
    @DatabaseField(columnName = AVATAR_FIELD_NAME)
    private String mAvatar;
    @DatabaseField(columnName = BACKGROUND_FILED_NAME)
    private String mBackground;

    public UserEntity() {

    }

    public UserEntity(String username, String password, String avatar, String background) {
        mUsername = username;
        mPassword = password;
        mAvatar = avatar;
        mBackground = background;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getBackground() {
        return mBackground;
    }

    public void setBackground(String background) {
        mBackground = background;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "mUsername='" + mUsername + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mAvatar='" + mAvatar + '\'' +
                ", mBackground='" + mBackground + '\'' +
                '}';
    }
}
