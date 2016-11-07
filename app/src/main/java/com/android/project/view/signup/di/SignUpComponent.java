package com.android.project.view.signup.di;

import com.android.project.view.signup.SignUpDialog;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Lobster on 05.11.16.
 */

@Singleton
@Subcomponent(modules = {SignUpModule.class})
public interface SignUpComponent {

    void inject(SignUpDialog dialog);

}
