package com.android.project.view.signin.di;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.view.signin.SignInPresenter;
import com.android.project.view.signin.SignInPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 05.11.16.
 */

@Module
public class SignInModule {

    private SignInPresenter.View mView;

    public SignInModule(SignInPresenter.View view) {
        mView = view;
    }

    @Provides
    public SignInPresenter.View provideView() {
        return mView;
    }

    @Provides
    public SignInPresenter.ActionListener providePresenter(RequestService requestService, DatabaseManager databaseManager, SignInPresenter.View view) {
        return new SignInPresenterImpl(requestService, databaseManager, view);
    }

    ;

}
