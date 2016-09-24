package com.android.project.signup;

import com.android.project.cofig.MainComponent;
import com.android.project.util.RequestService;

import javax.inject.Inject;

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
        requestService.checkName(name, new RequestService.Checking() {
            @Override
            public void checkResult(Integer requestCode) {
                mSignUpView.showCheckNameResult(requestCode);
            }
        });
    }

    @Override
    public void checkEmail(String email) {
        requestService.checkEmail(email, new RequestService.Checking() {
            @Override
            public void checkResult(Integer requestCode) {
                mSignUpView.showCheckEmailResult(requestCode);
            }
        });
    }

    @Override
    public void signUp(String name, String password, String email) {
        requestService.signUp(name, password, email);
    }


}
