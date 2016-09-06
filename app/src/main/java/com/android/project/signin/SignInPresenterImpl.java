package com.android.project.signin;

import com.android.project.cofig.DaggerMainComponent;
import com.android.project.util.RecordService;

import javax.inject.Inject;

/**
 * Created by Lobster on 05.09.16.
 */
public class SignInPresenterImpl implements SignInPresenter.ActionListener {

    private static final String TAG = SignInPresenter.class.getName();
    @Inject
    public RecordService recordService;
    private SignInPresenter.View mSignInView;

    public SignInPresenterImpl(SignInPresenter.View signInView) {
        mSignInView = signInView;
        DaggerMainComponent.create().inject(this);
    }

    @Override
    public void signIn(String name, String password) {
        recordService.signIn(name, password, new RecordService.SignInCallback() {
            @Override
            public void signInStatus(Integer requestCode) {
                mSignInView.openUserPage(requestCode);
            }
        });
    }
}
