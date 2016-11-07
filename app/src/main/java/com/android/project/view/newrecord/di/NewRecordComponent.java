package com.android.project.view.newrecord.di;

import com.android.project.view.newrecord.NewRecordActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Lobster on 06.11.16.
 */

@Singleton
@Subcomponent(modules = {NewRecordModule.class})
public interface NewRecordComponent {

    void inject(NewRecordActivity activity);

}
