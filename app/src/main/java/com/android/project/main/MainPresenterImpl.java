package com.android.project.main;

import com.android.project.cofig.DaggerMainComponent;
import com.android.project.model.User;
import com.android.project.util.RecordService;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by macos on 26.07.16.
 */

public class MainPresenterImpl implements MainPresenter.ActionListener {

    @Inject
    public RecordService recordService;

    private MainPresenter.View mMainView;

    public MainPresenterImpl(MainPresenter.View mainView) {
        mMainView = mainView;
        DaggerMainComponent.create().inject(this);
    }

    @Override
    public void loadUserInfo(String name) {
        recordService.getUserInfo(name, new RecordService.LoadUserInfo() {
            @Override
            public void onUserInfoLoaded(User user) {
                mMainView.showUserInfo(user);
            }
        });
    }

    @Override
    public void updateBackground(File imageFile, String name) {
        recordService.updateBackground(imageFile, name, new RecordService.LoadUserInfo() {
            @Override
            public void onUserInfoLoaded(User user) {
                mMainView.showUserInfo(user);
            }
        });
    }

    @Override
    public void updateAvatar(File imageFile, String name) {
        recordService.updateAvatar(imageFile, name, new RecordService.LoadUserInfo() {
            @Override
            public void onUserInfoLoaded(User user) {
                mMainView.showUserInfo(user);
            }
        });
    }
}
