package com.android.project.main;

import com.android.project.model.User;
import com.android.project.util.RecordService;

import java.io.File;

/**
 * Created by macos on 26.07.16.
 */
public class MainPresenterImpl implements MainPresenter.ActionListener {

    private RecordService mRecordService;
    private MainPresenter.View mMainView;

    public MainPresenterImpl(RecordService recordService, MainPresenter.View mainView) {
        mRecordService = recordService;
        mMainView = mainView;
    }

    @Override
    public void loadUserInfo(String name) {
        mRecordService.getUserInfo(name, new RecordService.LoadUserInfo() {
            @Override
            public void onUserInfoLoaded(User user) {
                mMainView.showUserInfo(user);
            }
        });
    }

    @Override
    public void updateBackground(File imageFile, String name) {
        mRecordService.updateBackground(imageFile, name, new RecordService.LoadUserInfo() {
            @Override
            public void onUserInfoLoaded(User user) {
                mMainView.showUserInfo(user);
            }
        });
    }

    @Override
    public void updateAvatar(File imageFile, String name) {
        mRecordService.updateAvatar(imageFile, name, new RecordService.LoadUserInfo() {
            @Override
            public void onUserInfoLoaded(User user) {
                mMainView.showUserInfo(user);
            }
        });
    }
}
