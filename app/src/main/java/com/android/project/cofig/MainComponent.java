package com.android.project.cofig;

import com.android.project.detail.DetailPresenterImpl;
import com.android.project.homewall.HomeWallPresenterImpl;
import com.android.project.login.SignInPresenterImpl;
import com.android.project.main.MainPresenterImpl;
import com.android.project.newitem.NewItemPresenterImpl;
import com.android.project.search.SearchPresenterImpl;
import com.android.project.wall.WallPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lobster on 09.08.16.
 */

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainPresenterImpl presenter);

    void inject(DetailPresenterImpl presenter);

    void inject(WallPresenterImpl presenter);

    void inject(NewItemPresenterImpl presenter);

    void inject(HomeWallPresenterImpl presenter);

    void inject(SearchPresenterImpl presenter);

    void ingect(SignInPresenterImpl presenter);

}
