package com.android.project.signup;

import com.android.project.cofig.DaggerMainComponent;
import com.android.project.util.RecordService;

import javax.inject.Inject;

/**
 * Created by Lobster on 05.09.16.
 */
public class SignUpPresenterImpl implements SignUpPresenter.ActionListener {

    private static final String TAG = SignUpPresenterImpl.class.getName();
    @Inject
    public RecordService recordService;
    private SignUpPresenter.View mSignUpView;

    public SignUpPresenterImpl(SignUpPresenter.View signUpView) {
        mSignUpView = signUpView;
        DaggerMainComponent.create().inject(this);
    }

    @Override
    public void checkName(String name) {
        recordService.checkName(name, new RecordService.Checking() {
            @Override
            public void checkResult(Integer requestCode) {
                mSignUpView.showCheckNameResult(requestCode);
            }
        });
    }

    @Override
    public void checkEmail(String email) {
        recordService.checkEmail(email, new RecordService.Checking() {
            @Override
            public void checkResult(Integer requestCode) {
                mSignUpView.showCheckEmailResult(requestCode);
            }
        });
    }

    @Override
    public void signUp(String name, String password, String email) {
        recordService.signUp(name, password, email, new RecordService.Checking() {
            @Override
            public void checkResult(Integer requestCode) {
                mSignUpView.openUserPage(requestCode);
            }
        });
    }


}
