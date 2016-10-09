package com.android.project.main;


import com.android.project.model.User;

import java.io.File;

/**
 * Created by Lobster on 26.07.16.
 */
public interface MainPresenter {

    interface View {
        void showUserInfo(User user);
    }

    interface ActionListener {
        void loadUserInfo(String name);

        void updateBackground(File imageFile, String name);

        void updateAvatar(File imageFile, String name);
    }
}
