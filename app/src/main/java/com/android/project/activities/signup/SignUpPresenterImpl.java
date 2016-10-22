package com.android.project.activities.signup;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 05.09.16.
 */
public class SignUpPresenterImpl implements SignUpPresenter.ActionListener {

    private static final String TAG = SignUpPresenterImpl.class.getSimpleName();
    @Inject
    public RequestService requestService;
    @Inject
    public CompositeSubscription compositeSubscription;

    private SignUpPresenter.View mSignUpView;

    public SignUpPresenterImpl(SignUpPresenter.View signUpView) {
        mSignUpView = signUpView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public void checkName(String name) {
        Log.i(TAG, "checkName: name - " + name);

        Subscription subscription =
                requestService
                        .checkName(name)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mSignUpView.showCheckNameResult(false);
                                Log.e(TAG, "checkName: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                Log.i(TAG, "checkName: SUCCESS");
                                mSignUpView.showCheckNameResult(true);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void checkEmail(String email) {
        Log.i(TAG, "checkEmail: email - " + email);

        Subscription subscription =
                requestService
                        .checkEmail(email)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mSignUpView.showCheckEmailResult(false);
                                Log.e(TAG, "checkEmail: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                Log.i(TAG, "checkEmail: SUCCESS");
                                mSignUpView.showCheckEmailResult(true);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void signUp(String name, String password, String email) {
        Log.i(TAG, String.format("signUp: name - %s, password - %s, email - %s", name, password, email));

        mSignUpView.showProgress();
        Subscription subscription =
                requestService
                        .signUp(name, password, email)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {
                                mSignUpView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mSignUpView.hideProgress();
                                mSignUpView.signUpResult(false);
                                Log.e(TAG, "signUp: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                Log.i(TAG, "signUp: SUCCESS");
                                mSignUpView.signUpResult(true);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
