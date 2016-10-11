package com.android.project.main;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.WhichOneApp;
import com.android.project.model.User;
import com.android.project.util.RequestService;

import java.io.File;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 26.07.16.
 */

public class MainPresenterImpl implements MainPresenter.ActionListener {

    private static final String TAG = MainPresenterImpl.class.getName();

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
        Subscription subscription =
                requestService
                        .getUserInfo(name)
                        .subscribe(new Observer<User>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(User user) {
                                mMainView.showUserInfo(user);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void updateBackground(File imageFile, String name) {
        Subscription subscription =
                requestService
                        .updateBackground(imageFile, name)
                        .subscribe(new Subscriber<User>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(User user) {
                                mMainView.showUserInfo(user);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void updateAvatar(File imageFile, String name) {
        Subscription subscription =
                requestService
                        .updateAvatar(imageFile, name)
                        .subscribe(new Subscriber<User>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(User user) {
                                mMainView.showUserInfo(user);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
