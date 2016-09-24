package com.android.project.cofig;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Lobster on 14.09.16.
 */

public class WhichOneApp extends Application {

    private MainComponent mMainComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mMainComponent = DaggerMainComponent.builder()
                .appModule(new AppModule(this))
                .build();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }

    public MainComponent getMainComponent() {
        return mMainComponent;
    }

}
