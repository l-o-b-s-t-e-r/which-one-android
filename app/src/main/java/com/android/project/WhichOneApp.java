package com.android.project;

import android.app.Application;
import android.content.Context;

import com.android.project.di.AppModule;
import com.android.project.di.DaggerMainComponent;
import com.android.project.di.MainComponent;
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
    private static MainComponent mMainComponent;

    public static Picasso getPicasso() {
        return mPicasso;
    }

    public static MainComponent getMainComponent() {
        return mMainComponent;
    }

    public static Context getContext() {
        return mContext;
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

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        mContext = getApplicationContext();

        mMainComponent = DaggerMainComponent.builder()
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
