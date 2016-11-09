package com.android.project.view.signin;

import com.android.project.model.User;
import com.android.project.util.CommonPresenter;

/**
 * Created by Lobster on 05.09.16.
 */
public interface SignInPresenter {
    interface View extends CommonPresenter.View {
        void setContentView();

        void openUserPage(User user);

        void onSuccessRemind(User user);

        void onErrorRemind(Throwable throwable);

        void onErrorSingIn(Throwable throwable);
    }

    interface ActionListener extends CommonPresenter.ActionListener {

        void signIn(String username);

        void signIn(String username, String password);

        void remindInfo(String email);

    }
}
