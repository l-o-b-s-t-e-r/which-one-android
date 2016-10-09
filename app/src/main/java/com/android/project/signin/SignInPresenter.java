package com.android.project.signin;

/**
 * Created by Lobster on 05.09.16.
 */
public interface SignInPresenter {
    interface View {
        void openUserPage(Boolean correctInfo);

        void remindInfoResult(Boolean correctInfo);
    }

    interface ActionListener {
        void signIn(String name, String password);

        void remindInfo(String email);
    }
}
