package com.android.project.view.signup;

import android.util.Log;

import com.android.project.api.RequestService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 05.09.16.
 */
public class SignUpPresenterImpl implements SignUpPresenter.ActionListener {

    private static final String TAG = SignUpPresenterImpl.class.getSimpleName();

    private SignUpPresenter.View mSignUpView;
    private RequestService mRequestService;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public SignUpPresenterImpl(RequestService requestService, SignUpPresenter.View signUpView) {
        mRequestService = requestService;
        mSignUpView = signUpView;
    }

    @Override
    public void checkName(String name) {
        Log.i(TAG, "checkName: name - " + name);

        Subscription subscription =
                mRequestService
                        .checkName(name)
                        .subscribe(
                                aVoid -> mSignUpView.showCheckNameResult(true),
                                throwable -> mSignUpView.showCheckNameResult(false)
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void checkEmail(String email) {
        Log.i(TAG, "checkEmail: email - " + email);

        Subscription subscription =
                mRequestService
                        .checkEmail(email)
                        .subscribe(aVoid -> mSignUpView.showCheckEmailResult(true),
                                throwable -> mSignUpView.showCheckEmailResult(false));

        compositeSubscription.add(subscription);
    }

    @Override
    public void signUp(String name, String password, String email) {
        Log.i(TAG, String.format("signUp: name - %s, password - %s, email - %s", name, password, email));

        Subscription subscription =
                mRequestService
                        .signUp(name, password, email)
                        .doOnSubscribe(mSignUpView::showProgress)
                        .doOnUnsubscribe(mSignUpView::hideProgress)
                        .subscribe(
                                aVoid -> mSignUpView.signUpResult(true),
                                throwable -> mSignUpView.signUpResult(false)
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
