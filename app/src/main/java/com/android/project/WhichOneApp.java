package com.android.project;

import android.app.Application;
import android.content.Context;

import com.android.project.di.AppComponent;
import com.android.project.di.AppModule;
import com.android.project.di.DaggerAppComponent;
import com.android.project.di.UserComponent;
import com.android.project.di.UserModule;
import com.android.project.model.User;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Lobster on 14.09.16.
 */

public class WhichOneApp extends Application {

    private static Context mContext;
    private static AppComponent mAppComponent;
    private static UserComponent mUserComponent;

    public static AppComponent getMainComponent() {
        return mAppComponent;
    }

    public static UserComponent getUserComponent() {
        return mUserComponent;
    }

    public static Context getContext() {
        return mContext;
    }

    public static UserComponent createUserComponent(User user) {
        mUserComponent = mAppComponent.plus(new UserModule(user));

        return mUserComponent;
    }

    public static void releaseUserComponent() {
        mUserComponent = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        mContext = getApplicationContext();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

}
