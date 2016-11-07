package com.android.project.view.search;

import com.android.project.model.User;
import com.android.project.util.CommonPresenter;

import java.util.List;

/**
 * Created by Lobster on 01.08.16.
 */
public interface SearchPresenter {

    interface View extends CommonPresenter.View {

        void showUsers(List<User> users);

        void showUserPage(User user);

    }

    interface ActionListener extends CommonPresenter.ActionListener {

        void loadUsers(String searchQuery);

        void loadNextUsers(String searchQuery, String lastLoadedUsername);

        void loadUserPage(User user);

    }
}
