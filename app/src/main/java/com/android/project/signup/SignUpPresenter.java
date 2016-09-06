package com.android.project.signup;

/**
 * Created by Lobster on 05.09.16.
 */
public interface SignUpPresenter {
    interface View {
        void showCheckNameResult(Integer requestCode);

        void openUserPage(Integer requestCode);
    }

    interface ActionListener {
        void checkName(String name);

        void signUp(String name, String password);
    }
}
