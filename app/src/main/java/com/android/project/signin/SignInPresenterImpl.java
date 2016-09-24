package com.android.project.signin;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.MainComponent;
import com.android.project.util.RequestService;

import javax.inject.Inject;

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
        requestService.signIn(name, password, new RequestService.Checking() {
            @Override
            public void checkResult(Integer requestCode) {
                mSignInView.openUserPage(requestCode);
            }
        });
    }

    @Override
    public void remindInfo(String email) {
        requestService.remindInfo(email, new RequestService.Checking() {
            @Override
            public void checkResult(Integer requestCode) {
                mSignInView.remindInfoResult(requestCode);
            }
        });
    }


}
