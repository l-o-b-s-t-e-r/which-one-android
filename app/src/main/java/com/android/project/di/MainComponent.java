package com.android.project.di;

import com.android.project.activities.detail.RecordDetailPresenterImpl;
import com.android.project.activities.homewall.HomeWallPresenterImpl;
import com.android.project.activities.main.MainPresenterImpl;
import com.android.project.activities.newrecord.NewRecordPresenterImpl;
import com.android.project.activities.search.SearchPresenterImpl;
import com.android.project.activities.signin.SignInPresenterImpl;
import com.android.project.activities.signup.SignUpPresenterImpl;
import com.android.project.activities.wall.WallPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lobster on 09.08.16.
 */

@Singleton
@Component(modules = {AppModule.class, MainModule.class})
public interface MainComponent {

    void inject(MainPresenterImpl presenter);

    void inject(RecordDetailPresenterImpl presenter);

    void inject(WallPresenterImpl presenter);

    void inject(NewRecordPresenterImpl presenter);

    void inject(HomeWallPresenterImpl presenter);

    void inject(SearchPresenterImpl presenter);

    void inject(SignInPresenterImpl presenter);

    void inject(SignUpPresenterImpl presenter);

}
