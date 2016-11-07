package com.android.project.view.search.di;

import com.android.project.api.RequestService;
import com.android.project.view.search.SearchPresenter;
import com.android.project.view.search.SearchPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 06.11.16.
 */

@Module
public class SearchModule {

    private SearchPresenter.View mView;

    public SearchModule(SearchPresenter.View view) {
        mView = view;
    }

    @Provides
    public SearchPresenter.View provideView() {
        return mView;
    }

    @Provides
    SearchPresenter.ActionListener providePresenter(RequestService requestService, SearchPresenter.View view) {
        return new SearchPresenterImpl(requestService, view);
    }
}
