package com.android.project.di;

import com.android.project.view.signin.di.SignInComponent;
import com.android.project.view.signin.di.SignInModule;
import com.android.project.view.signup.di.SignUpComponent;
import com.android.project.view.signup.di.SignUpModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lobster on 09.08.16.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    UserComponent plus(UserModule module);

    SignInComponent plus(SignInModule module);

    SignUpComponent plus(SignUpModule module);

}
