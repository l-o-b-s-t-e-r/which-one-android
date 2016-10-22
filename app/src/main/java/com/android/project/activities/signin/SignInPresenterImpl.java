package com.android.project.activities.signin;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 05.09.16.
 */

public class SignInPresenterImpl implements SignInPresenter.ActionListener {

    private static final String TAG = SignInPresenterImpl.class.getSimpleName();

    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;
    @Inject
    public CompositeSubscription compositeSubscription;

    private SignInPresenter.View mSignInView;

    public SignInPresenterImpl(SignInPresenter.View signInView) {
        mSignInView = signInView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public void signIn(String name, String password) {
        Log.i(TAG, String.format("signIn: name - %s, password - %s", name, password));

        mSignInView.showProgress();
        Subscription subscription =
                requestService
                        .signIn(name, password)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {
                                mSignInView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mSignInView.hideProgress();
                                mSignInView.openUserPage(false);
                                Log.e(TAG, "signIn: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                Log.i(TAG, "signIn: SUCCESS");
                                mSignInView.openUserPage(true);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void remindInfo(String email) {
        Log.i(TAG, String.format("remindInfo: email - %s", email));

        Subscription subscription =
                requestService
                        .remindInfo(email)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mSignInView.remindInfoResult(false);
                                Log.e(TAG, "remindInfo: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                Log.i(TAG, "remindInfo: SUCCESS");
                                mSignInView.remindInfoResult(true);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
