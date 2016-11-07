package com.android.project.view.newrecord.di;

import com.android.project.api.RequestService;
import com.android.project.view.newrecord.NewRecordPresenter;
import com.android.project.view.newrecord.NewRecordPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lobster on 06.11.16.
 */

@Module
public class NewRecordModule {

    private NewRecordPresenter.View mView;

    public NewRecordModule(NewRecordPresenter.View view) {
        mView = view;
    }

    @Provides
    public NewRecordPresenter.View provideView() {
        return mView;
    }

    @Provides
    public NewRecordPresenter.ActionListener providePresenter(RequestService requestService, NewRecordPresenter.View view) {
        return new NewRecordPresenterImpl(requestService, view);
    }

}
