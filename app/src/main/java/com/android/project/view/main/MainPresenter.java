package com.android.project.view.main;


import com.android.project.model.User;
import com.android.project.util.CommonPresenter;

import java.io.File;

/**
 * Created by Lobster on 26.07.16.
 */
public interface MainPresenter {

    interface View extends CommonPresenter.View {

        void showUserInfo(User user);

        void updateAvatar(User user);

        void updateBackground(User user);

        void signOut();

    }

    interface ActionListener extends CommonPresenter.ActionListener {

        void loadUserInfo(String username);

        void updateAvatar(File imageFile, String name);

        void updateBackground(File imageFile, String name);

        void clearDatabase();

    }
}
