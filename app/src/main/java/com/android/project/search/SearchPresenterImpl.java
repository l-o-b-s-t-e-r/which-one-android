package com.android.project.search;

import com.android.project.cofig.DaggerMainComponent;
import com.android.project.model.User;
import com.android.project.util.RecordService;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Lobster on 01.08.16.
 */

public class SearchPresenterImpl implements SearchPresenter.ActionListener {

    private static final String TAG = SearchPresenterImpl.class.getName();
    @Inject
    public RecordService recordService;
    private SearchPresenter.View mSearchView;

    public SearchPresenterImpl(SearchPresenter.View searchView) {
        mSearchView = searchView;
        DaggerMainComponent.create().inject(this);
    }

    @Override
    public void loadUsers(String searchQuery) {
        recordService.getUsers(searchQuery, new RecordService.LoadUsers() {
            @Override
            public void usersLoaded(List<User> user) {
                mSearchView.showUsers(user);
            }
        });
    }

    @Override
    public void loadNextUsers(String searchQuery, Long lastLoadedUserId) {
        recordService.getUsersFromId(searchQuery, lastLoadedUserId, new RecordService.LoadUsers() {
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
