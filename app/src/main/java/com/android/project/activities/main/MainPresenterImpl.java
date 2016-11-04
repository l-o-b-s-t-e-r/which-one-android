package com.android.project.activities.main;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.util.ImageManager;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 26.07.16.
 */

public class MainPresenterImpl implements MainPresenter.ActionListener {

    private static final String TAG = MainPresenterImpl.class.getSimpleName();

    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;
    @Inject
    public CompositeSubscription compositeSubscription;

    private MainPresenter.View mMainView;

    public MainPresenterImpl(MainPresenter.View mainView) {
        mMainView = mainView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public void loadUserInfo(String name) {
        Log.i(TAG, "loadUserInfo: name - " + name);

        Subscription subscription =
                requestService
                        .getUserInfo(name)
                        .subscribe(
                                mMainView::showUserInfo,
                                Throwable::printStackTrace

                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void updateBackground(File imageFile, final String name) {
        Log.i(TAG, String.format("updateBackground: file - %s, name - %s", imageFile.getAbsolutePath(), name));

        Subscription subscription =
                resizeImage(imageFile)
                        .subscribeOn(Schedulers.io())
                        .flatMap(file -> requestService.updateBackground(file, name))
                        .subscribe(
                                mMainView::showUserInfo,
                                Throwable::printStackTrace
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void updateAvatar(File imageFile, final String name) {
        Log.i(TAG, String.format("updateAvatar: file - %s, name - %s", imageFile.getAbsolutePath(), name));

        Subscription subscription =
                cropImage(imageFile)
                        .subscribeOn(Schedulers.io())
                        .flatMap(file -> requestService.updateAvatar(file, name))
                        .subscribe(
                                mMainView::showUserInfo,
                                Throwable::printStackTrace
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }

    private Observable<File> cropImage(final File imageFile) {
        return Observable.defer(() -> Observable.just(ImageManager.getInstance().cropImageAsSquare(imageFile)));
    }

    private Observable<File> resizeImage(final File imageFile) {
        return Observable.defer(() -> Observable.just(ImageManager.getInstance().resizeImage(imageFile)));
    }
}
