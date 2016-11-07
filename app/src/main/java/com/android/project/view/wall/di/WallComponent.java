package com.android.project.view.wall.di;

import com.android.project.view.wall.WallFragment;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Lobster on 05.11.16.
 */

@Singleton
@Subcomponent(modules = {WallModule.class})
public interface WallComponent {

    void inject(WallFragment fragment);

}
