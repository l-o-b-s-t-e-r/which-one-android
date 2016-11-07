package com.android.project.view.signup.di;

import com.android.project.api.RequestService;
import com.android.project.view.signup.SignUpPresenter;
import com.android.project.view.signup.SignUpPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 05.11.16.
 */

@Module
public class SignUpModule {

    private SignUpPresenter.View mView;

    public SignUpModule(SignUpPresenter.View view) {
        mView = view;
    }

    @Provides
    public SignUpPresenter.View provideView() {
        return mView;
    }

    @Provides
    public SignUpPresenter.ActionListener providePresenter(RequestService requestService, SignUpPresenter.View view) {
        return new SignUpPresenterImpl(requestService, view);
    }

}
