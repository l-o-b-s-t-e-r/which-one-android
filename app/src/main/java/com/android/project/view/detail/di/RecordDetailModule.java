package com.android.project.view.detail.di;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.view.detail.RecordDetailPresenter;
import com.android.project.view.detail.RecordDetailPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 06.11.16.
 */

@Module
public class RecordDetailModule {

    private RecordDetailPresenter.View mView;

    public RecordDetailModule(RecordDetailPresenter.View view) {
        mView = view;
    }

    @Provides
    public RecordDetailPresenter.View provideView() {
        return mView;
    }

    @Provides
    public RecordDetailPresenter.ActionListener providePresenter(RequestService requestService, DatabaseManager databaseManager, RecordDetailPresenter.View view) {
        return new RecordDetailPresenterImpl(requestService, databaseManager, view);
    }

}
