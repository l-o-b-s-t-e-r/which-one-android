package com.android.project.signin;

import com.android.project.util.CommonPresenter;

/**
 * Created by Lobster on 05.09.16.
 */
public interface SignInPresenter {
    interface View extends CommonPresenter.View {
        void openUserPage(Boolean correctInfo);

        void remindInfoResult(Boolean correctInfo);
    }

    interface ActionListener extends CommonPresenter.ActionListener {
        void signIn(String name, String password);

        void remindInfo(String email);
    }
}
