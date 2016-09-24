package com.android.project.search;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.MainComponent;
import com.android.project.model.User;
import com.android.project.util.RequestService;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Lobster on 01.08.16.
 */

public class SearchPresenterImpl implements SearchPresenter.ActionListener {

    private static final String TAG = SearchPresenterImpl.class.getName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;

    private SearchPresenter.View mSearchView;

    public SearchPresenterImpl(SearchPresenter.View searchView, MainComponent mainComponent) {
        mSearchView = searchView;
        mainComponent.inject(this);
    }

    @Override
    public void loadUsers(String searchQuery) {
        requestService.getUsers(searchQuery, new RequestService.LoadUsers() {
            @Override
            public void usersLoaded(List<User> user) {
                mSearchView.showUsers(user);
            }
        });
    }

    @Override
    public void loadNextUsers(String searchQuery, Long lastLoadedUserId) {
        requestService.getUsersFromId(searchQuery, lastLoadedUserId, new RequestService.LoadUsers() {
            @Override
            public void usersLoaded(List<User> user) {
                mSearchView.showUsers(user);
            }
        });
    }

    @Override
    public void loadUserPage(User user) {
        mSearchView.showUserPage(user);
    }
}
