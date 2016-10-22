package com.android.project;

import android.app.Application;

import com.android.project.di.AppModule;
import com.android.project.di.DaggerMainComponent;
import com.android.project.di.MainComponent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;

/**
 * Created by Lobster on 14.09.16.
 */

public class WhichOneApp extends Application {

    private static Picasso mPicasso;
    private static MainComponent mMainComponent;

    public static Picasso getPicasso() {
        return mPicasso;
    }

    public static MainComponent getMainComponent() {
        return mMainComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        mMainComponent = DaggerMainComponent.builder()
                .appModule(new AppModule(this))
                .build();

        mPicasso = Picasso.with(this);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }

}
