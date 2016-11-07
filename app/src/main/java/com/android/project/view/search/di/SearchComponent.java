package com.android.project.view.search.di;

import com.android.project.view.search.SearchActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Lobster on 06.11.16.
 */

@Singleton
@Subcomponent(modules = {SearchModule.class})
public interface SearchComponent {

    void inject(SearchActivity activity);

}
