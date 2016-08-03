package com.android.project.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Lobster on 01.04.16.
 */

public class UserApp implements Serializable {

    @DatabaseField(generatedId = true, columnName = "user_id")
    private long mUserId;

    @DatabaseField(columnName = "user_name")
    private String mUserName;

    @DatabaseField(columnName = "user_password")
    private String mUserPassword;

    public UserApp() {

    }

    public long getmUserId() {
        return mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String userName) {
        this.mUserName = userName;
    }

    public String getmUserPassword() {
        return mUserPassword;
    }

    public void setmUserPassword(String userPassword) {
        this.mUserPassword = userPassword;
    }
}
