package com.android.project.view.homewall.di;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.view.homewall.HomeWallPresenter;
import com.android.project.view.homewall.HomeWallPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 05.11.16.
 */
@Module
public class HomeWallModule {

    private HomeWallPresenter.View mView;

    public HomeWallModule(HomeWallPresenter.View view) {
        mView = view;
    }

    @Provides
    public HomeWallPresenter.View provideView() {
        return mView;
    }

    @Provides
    public HomeWallPresenter.ActionListener providePresenter(RequestService requestService, DatabaseManager databaseManager, HomeWallPresenter.View view) {
        return new HomeWallPresenterImpl(requestService, databaseManager, view);
    }
}
