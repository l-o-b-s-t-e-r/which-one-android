package com.android.project.search;

import com.android.project.model.User;

import java.util.List;

/**
 * Created by Lobster on 01.08.16.
 */
public interface SearchPresenter {

    interface View {

        void showUsers(List<User> users);

        void showUserPage(User user);

    }

    interface ActionListener {

        void loadUsers(String searchQuery);

        void loadNextUsers(String searchQuery, Long lastLoadedUserId);

        void loadUserPage(User user);

    }
}
