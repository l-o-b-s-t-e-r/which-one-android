package com.android.project;

import android.app.Application;
import android.content.Context;

import com.android.project.di.AppComponent;
import com.android.project.di.AppModule;
import com.android.project.di.DaggerAppComponent;
import com.android.project.di.UserComponent;
import com.android.project.di.UserModule;
import com.android.project.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Lobster on 14.09.16.
 */

public class WhichOneApp extends Application {

    private static Context mContext;
    private static Picasso mPicasso;
    private static AppComponent mAppComponent;
    private static UserComponent mUserComponent;

    public static Picasso getPicasso() {
        return mPicasso;
    }

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

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    //<-- TEMPORARY -->>//

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        mContext = getApplicationContext();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        LruCache picassoLruCache = new LruCache(this);
        picassoLruCache.clear();
        clearImageDiskCache();

        //mPicasso = Picasso.with(this);
        mPicasso = new Picasso.Builder(this)
                .memoryCache(picassoLruCache)
                .build();

        mPicasso.setIndicatorsEnabled(true);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }

    public boolean clearImageDiskCache() {
        File cache = new File(this.getCacheDir(), "picasso-cache");
        if (cache.exists() && cache.isDirectory()) {
            return deleteDir(cache);
        }
        return false;
    }

}
