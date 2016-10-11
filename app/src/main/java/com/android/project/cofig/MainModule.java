package com.android.project.cofig;

import android.app.Application;

import com.android.project.util.RequestService;
import com.android.project.util.RequestServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 09.08.16.
 */

@Module
public class MainModule {

    @Provides
    @Singleton
    public RequestService provideRecordService() {
        return new RequestServiceImpl();
    }

    @Provides
    @Singleton
    public DatabaseManager provideMainRecordService(Application application) {
        return new DatabaseManagerImpl(application);
    }

    @Provides
    public CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }
}
