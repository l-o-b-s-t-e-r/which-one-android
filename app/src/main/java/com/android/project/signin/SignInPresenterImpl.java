package com.android.project.signin;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.MainComponent;
import com.android.project.util.RequestService;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Lobster on 05.09.16.
 */
public class SignInPresenterImpl implements SignInPresenter.ActionListener {

    private static final String TAG = SignInPresenterImpl.class.getName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;

    private SignInPresenter.View mSignInView;

    public SignInPresenterImpl(SignInPresenter.View signInView, MainComponent mainComponent) {
        mSignInView = signInView;
        mainComponent.inject(this);
    }

    @Override
    public void signIn(String name, String password) {
        requestService
                .signIn(name, password)
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSignInView.openUserPage(false);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        mSignInView.openUserPage(true);
                    }
                });
    }

    @Override
    public void remindInfo(String email) {
        requestService
                .remindInfo(email)
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSignInView.remindInfoResult(false);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        mSignInView.remindInfoResult(true);
                    }
                });

    }


}
