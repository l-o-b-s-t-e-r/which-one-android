package com.android.project.view.main;

import android.util.Log;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.util.ImageManager;

import java.io.File;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 26.07.16.
 */

public class MainPresenterImpl implements MainPresenter.ActionListener {

    private static final String TAG = MainPresenterImpl.class.getSimpleName();

    private RequestService mRequestService;
    private DatabaseManager mDatabaseManager;
    private MainPresenter.View mMainView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public MainPresenterImpl(RequestService requestService, DatabaseManager databaseManager, MainPresenter.View mainView) {
        mRequestService = requestService;
        mDatabaseManager = databaseManager;
        mMainView = mainView;
    }

    @Override
    public void loadUserInfo(String name) {
        Log.i(TAG, "loadUserInfo: name - " + name);

        Subscription subscription =
                mRequestService
                        .getUserInfo(name)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mMainView::showUserInfo,
                                mMainView::onError

                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void updateBackground(File imageFile, final String name) {
        Log.i(TAG, String.format("updateBackground: file - %s, name - %s", imageFile.getAbsolutePath(), name));

        Subscription subscription =
                ImageManager.getInstance().resizeImage(imageFile)
                        .flatMap(file -> mRequestService.updateBackground(file, name))
                        .flatMap(mDatabaseManager::save)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mMainView::updateBackground,
                                mMainView::onError
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void updateAvatar(File imageFile, final String name) {
        Log.i(TAG, String.format("updateAvatar: file - %s, name - %s", imageFile.getAbsolutePath(), name));

        Subscription subscription =
                ImageManager.getInstance().cropImageAsSquare(imageFile)
                        .flatMap(file -> mRequestService.updateAvatar(file, name)
                                .flatMap(mDatabaseManager::save))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mMainView::updateAvatar,
                                mMainView::onError
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void clearDatabase() {
        Subscription subscription =
                mDatabaseManager.clearAll()
                        .doOnSubscribe(mMainView::showProgress)
                        .subscribe(
                                object -> mMainView.signOut()
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void stop() {
        compositeSubscription.clear();
    }
}
