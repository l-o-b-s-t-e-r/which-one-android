package com.android.project.view.signin.di;

import com.android.project.view.signin.SignInActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Lobster on 05.11.16.
 */
@Singleton
@Subcomponent(modules = {SignInModule.class})
public interface SignInComponent {

    void inject(SignInActivity activity);

}
