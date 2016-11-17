package com.android.project.view.signin;

import android.util.Log;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 05.09.16.
 */

public class SignInPresenterImpl implements SignInPresenter.ActionListener {

    private static final String TAG = SignInPresenterImpl.class.getSimpleName();

    private RequestService mRequestService;
    private DatabaseManager mDatabaseManager;
    private SignInPresenter.View mSignInView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    public SignInPresenterImpl(RequestService requestService, DatabaseManager databaseManager, SignInPresenter.View signInView) {
        mRequestService = requestService;
        mDatabaseManager = databaseManager;
        mSignInView = signInView;
    }

    @Override
    public void signIn(String username) {
        Log.i(TAG, String.format("signIn: name - %s", username));

        Subscription subscription =
                mDatabaseManager.getUserByName(username)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mSignInView::openUserPage,
                                throwable -> mSignInView.setContentView()
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void signIn(String username, String password) {
        Log.i(TAG, String.format("signIn: name - %s, password - %s", username, password));

        Subscription subscription =
                mRequestService
                        .signIn(username, password)
                        .doOnSubscribe(mSignInView::showProgress)
                        .flatMap(mDatabaseManager::save)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mSignInView::openUserPage,
                                throwable -> {
                                    mSignInView.onErrorSingIn(throwable);
                                    mSignInView.hideProgress();
                                }
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void remindInfo(String email) {
        Log.i(TAG, String.format("remindInfo: email - %s", email));

        Subscription subscription =
                mRequestService
                        .remindInfo(email)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mSignInView::onSuccessRemind,
                                mSignInView::onErrorRemind

                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void stop() {
        compositeSubscription.clear();
    }
}
