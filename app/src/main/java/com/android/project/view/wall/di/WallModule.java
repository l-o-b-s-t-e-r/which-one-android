package com.android.project.view.wall.di;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.view.wall.WallPresenter;
import com.android.project.view.wall.WallPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 05.11.16.
 */

@Module
public class WallModule {

    private WallPresenter.View mView;

    public WallModule(WallPresenter.View view) {
        mView = view;
    }

    @Provides
    public WallPresenter.View provideView() {
        return mView;
    }

    @Provides
    public WallPresenter.ActionListener providePresenter(RequestService requestService, DatabaseManager databaseManager, WallPresenter.View view) {
        return new WallPresenterImpl(requestService, databaseManager, view);
    }
}
