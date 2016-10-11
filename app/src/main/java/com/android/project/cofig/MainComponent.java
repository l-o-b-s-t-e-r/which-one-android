package com.android.project.cofig;

import com.android.project.detail.RecordDetailPresenterImpl;
import com.android.project.homewall.HomeWallPresenterImpl;
import com.android.project.main.MainPresenterImpl;
import com.android.project.newitem.NewItemPresenterImpl;
import com.android.project.search.SearchPresenterImpl;
import com.android.project.signin.SignInPresenterImpl;
import com.android.project.signup.SignUpPresenterImpl;
import com.android.project.wall.WallPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lobster on 09.08.16.
 */

@Singleton
@Component(modules = {MainModule.class, AppModule.class})
public interface MainComponent {

    void inject(MainPresenterImpl presenter);

    void inject(RecordDetailPresenterImpl presenter);

    void inject(WallPresenterImpl presenter);

    void inject(NewItemPresenterImpl presenter);

    void inject(HomeWallPresenterImpl presenter);

    void inject(SearchPresenterImpl presenter);

    void inject(SignInPresenterImpl presenter);

    void inject(SignUpPresenterImpl presenter);

}
