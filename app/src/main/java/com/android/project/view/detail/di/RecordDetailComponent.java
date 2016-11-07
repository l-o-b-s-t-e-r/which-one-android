package com.android.project.view.detail.di;

import com.android.project.view.detail.RecordDetailActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Lobster on 06.11.16.
 */

@Singleton
@Subcomponent(modules = {RecordDetailModule.class})
public interface RecordDetailComponent {

    void inject(RecordDetailActivity activity);

}
