package com.android.project.signup;

import com.android.project.cofig.MainComponent;
import com.android.project.util.RequestService;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Lobster on 05.09.16.
 */
public class SignUpPresenterImpl implements SignUpPresenter.ActionListener {

    private static final String TAG = SignUpPresenterImpl.class.getName();
    @Inject
    public RequestService requestService;

    private SignUpPresenter.View mSignUpView;

    public SignUpPresenterImpl(SignUpPresenter.View signUpView, MainComponent mainComponent) {
        mSignUpView = signUpView;
        mainComponent.inject(this);
    }

    @Override
    public void checkName(String name) {
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
    }

    @Override
    public void checkEmail(String email) {
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
    }

    @Override
    public void signUp(String name, String password, String email) {
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
    }


}
