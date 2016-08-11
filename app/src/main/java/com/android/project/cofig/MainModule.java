package com.android.project.cofig;

import com.android.project.util.RecordService;
import com.android.project.util.RecordServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 09.08.16.
 */

@Module
public class MainModule {

    @Provides
    @Singleton
    public RecordService provideRecordService() {
        return new RecordServiceImpl();
    }
}
