package com.android.project.signin;

/**
 * Created by Lobster on 05.09.16.
 */
public interface SignInPresenter {
    interface View {
        void openUserPage(Integer requestCode);

        void remindInfoResult(Integer requestCode);
    }

    interface ActionListener {
        void signIn(String name, String password);

        void remindInfo(String email);
    }
}
