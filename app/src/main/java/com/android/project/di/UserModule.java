package com.android.project.di;

import com.android.project.model.User;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 05.11.16.
 */
@Module
public class UserModule {

    private User mUser;

    public UserModule(User user) {
        mUser = user;
    }

    @Provides
    @Singleton
    public User provideUser() {
        return mUser;
    }

}
