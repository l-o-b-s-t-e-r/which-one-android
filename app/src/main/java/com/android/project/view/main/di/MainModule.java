package com.android.project.view.main.di;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.view.main.MainPresenter;
import com.android.project.view.main.MainPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 04.11.16.
 */

@Module
public class MainModule {

    private MainPresenter.View mView;

    public MainModule(MainPresenter.View view) {
        mView = view;
    }

    @Provides
    public MainPresenter.View provideView() {
        return mView;
    }

    @Provides
    public MainPresenter.ActionListener provideMainPresenter(RequestService requestService, DatabaseManager databaseManager, MainPresenter.View view) {
        return new MainPresenterImpl(requestService, databaseManager, view);
    }
}
