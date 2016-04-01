package com.android.project;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Lobster on 01.04.16.
 */

public class User implements Serializable{

    @DatabaseField(generatedId = true, columnName = "user_id")
    private long mUserId;

    @DatabaseField(columnName = "user_name")
    private String mUserName;

    @DatabaseField(columnName = "user_password")
    private String mUserPassword;

    public User(){

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
