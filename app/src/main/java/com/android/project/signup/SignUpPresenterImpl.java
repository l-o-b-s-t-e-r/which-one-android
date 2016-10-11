package com.android.project.signup;

import com.android.project.cofig.WhichOneApp;
import com.android.project.util.RequestService;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 05.09.16.
 */
public class SignUpPresenterImpl implements SignUpPresenter.ActionListener {

    private static final String TAG = SignUpPresenterImpl.class.getName();
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
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                mSignUpView.showCheckNameResult(true);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void checkEmail(String email) {
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
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                mSignUpView.showCheckEmailResult(true);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void signUp(String name, String password, String email) {
        Subscription subscription =
                requestService
                        .signUp(name, password, email)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mSignUpView.signUpResult(false);
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
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
