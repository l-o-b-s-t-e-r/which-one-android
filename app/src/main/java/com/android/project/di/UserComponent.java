package com.android.project.di;

import com.android.project.view.detail.di.RecordDetailComponent;
import com.android.project.view.detail.di.RecordDetailModule;
import com.android.project.view.homewall.di.HomeWallComponent;
import com.android.project.view.homewall.di.HomeWallModule;
import com.android.project.view.main.di.MainComponent;
import com.android.project.view.main.di.MainModule;
import com.android.project.view.newrecord.di.NewRecordComponent;
import com.android.project.view.newrecord.di.NewRecordModule;
import com.android.project.view.search.di.SearchComponent;
import com.android.project.view.search.di.SearchModule;
import com.android.project.view.wall.di.WallComponent;
import com.android.project.view.wall.di.WallModule;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Lobster on 05.11.16.
 */

@Singleton
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {

    MainComponent plus(MainModule module);

    WallComponent plus(WallModule module);

    SearchComponent plus(SearchModule module);

    HomeWallComponent plus(HomeWallModule module);

    NewRecordComponent plus(NewRecordModule module);

    RecordDetailComponent plus(RecordDetailModule module);

}
