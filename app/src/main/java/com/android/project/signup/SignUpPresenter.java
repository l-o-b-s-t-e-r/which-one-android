package com.android.project.signup;

/**
 * Created by Lobster on 05.09.16.
 */
public interface SignUpPresenter {
    interface View {
        void showCheckNameResult(Integer requestCode);

        void showCheckEmailResult(Integer requestCode);
    }

    interface ActionListener {
        void checkName(String name);

        void checkEmail(String email);

        void signUp(String name, String password, String email);
    }
}
