package com.android.project.activities.main;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.User;
import com.android.project.util.ImageManager;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;
import rx.functions.Func1;
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
                        .subscribe(new Observer<User>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "loadUserInfo: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(User user) {
                                Log.i(TAG, "loadUserInfo: (LOADED) user - " + user.toString());
                                mMainView.showUserInfo(user);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void updateBackground(File imageFile, String name) {
        Log.i(TAG, String.format("updateBackground: file - %s, name - %s", imageFile.getAbsolutePath(), name));

        Subscription subscription =
                requestService
                        .updateBackground(imageFile, name)
                        .subscribe(new Subscriber<User>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "updateBackground: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(User user) {
                                Log.i(TAG, "updateBackground: (SUCCESS) user - " + user.toString());
                                mMainView.showUserInfo(user);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void updateAvatar(File imageFile, final String name) {
        Log.i(TAG, String.format("updateAvatar: file - %s, name - %s", imageFile.getAbsolutePath(), name));

        Subscription subscription =
                cropImage(imageFile)
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Func1<File, Observable<User>>() {
                            @Override
                            public Observable<User> call(File file) {
                                return requestService.updateAvatar(file, name);
                            }
                        })
                        .subscribe(new Subscriber<User>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "updateAvatar: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(User user) {
                                Log.i(TAG, "updateAvatar: (SUCCESS) user - " + user.toString());
                                mMainView.showUserInfo(user);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }

    private Observable<File> cropImage(final File imageFile) {
        return Observable.defer(new Func0<Observable<File>>() {
            @Override
            public Observable<File> call() {
                return Observable.just(ImageManager.getInstance().cropImageAsSquare(imageFile));
            }
        });
    }
}
