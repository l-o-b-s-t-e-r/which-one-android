package com.android.project.main;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.MainComponent;
import com.android.project.model.User;
import com.android.project.util.RequestService;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by macos on 26.07.16.
 */

public class MainPresenterImpl implements MainPresenter.ActionListener {

    private static final String TAG = MainPresenterImpl.class.getName();

    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;

    private MainPresenter.View mMainView;

    public MainPresenterImpl(MainPresenter.View mainView, MainComponent mainComponent) {
        mMainView = mainView;
        mainComponent.inject(this);
    }

    @Override
    public void loadUserInfo(String name) {
        requestService.getUserInfo(name, new RequestService.LoadUserInfo() {
            @Override
            public void onUserInfoLoaded(User user) {
                mMainView.showUserInfo(user);
            }
        });
    }

    @Override
    public void updateBackground(File imageFile, String name) {
        requestService.updateBackground(imageFile, name, new RequestService.LoadUserInfo() {
            @Override
            public void onUserInfoLoaded(User user) {
                mMainView.showUserInfo(user);
            }
        });
    }

    @Override
    public void updateAvatar(File imageFile, String name) {
        requestService.updateAvatar(imageFile, name, new RequestService.LoadUserInfo() {
            @Override
            public void onUserInfoLoaded(User user) {
                mMainView.showUserInfo(user);
            }
        });
    }
}
