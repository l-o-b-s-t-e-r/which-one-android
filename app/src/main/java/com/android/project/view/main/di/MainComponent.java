package com.android.project.view.main.di;

import com.android.project.view.main.MainActivity;
import com.android.project.view.userpage.UserPageActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Lobster on 04.11.16.
 */

@Singleton
@Subcomponent(modules = {MainModule.class})
public interface MainComponent {

    void inject(MainActivity activity);

    void inject(UserPageActivity activity);

}
