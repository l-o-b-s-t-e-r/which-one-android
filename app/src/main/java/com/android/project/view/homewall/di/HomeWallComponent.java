package com.android.project.view.homewall.di;

import com.android.project.view.homewall.HomeWallFragment;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Lobster on 05.11.16.
 */
@Singleton
@Subcomponent(modules = {HomeWallModule.class})
public interface HomeWallComponent {

    void inject(HomeWallFragment fragment);

}
