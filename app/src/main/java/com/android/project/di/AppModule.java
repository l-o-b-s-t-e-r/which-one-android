package com.android.project.di;

import android.app.Application;

import com.android.project.api.RequestService;
import com.android.project.api.RequestServiceImpl;
import com.android.project.database.DatabaseHelper;
import com.android.project.database.DatabaseManager;
import com.android.project.database.DatabaseManagerImpl;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 14.09.16.
 */

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public DatabaseManager provideDatabaseManager(Application application) {
        return new DatabaseManagerImpl(OpenHelperManager.getHelper(application, DatabaseHelper.class));
    }

    @Provides
    @Singleton
    public RequestService provideRequestService() {
        return new RequestServiceImpl(RequestServiceImpl.BASE_URL);
    }

}
