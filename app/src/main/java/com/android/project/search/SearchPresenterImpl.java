package com.android.project.search;

import com.android.project.model.User;
import com.android.project.util.RecordService;
import com.android.project.util.RecordServiceImpl;

import java.util.List;

/**
 * Created by Lobster on 01.08.16.
 */

public class SearchPresenterImpl implements SearchPresenter.ActionListener {

    private static final String TAG = SearchPresenterImpl.class.getName();
    private RecordServiceImpl mRecordService;
    private SearchPresenter.View mSearchView;

    public SearchPresenterImpl(RecordServiceImpl recordService, SearchPresenter.View searchView) {
        mRecordService = recordService;
        mSearchView = searchView;
    }

    @Override
    public void loadUsers(String searchQuery) {
        mRecordService.getUsers(searchQuery, new RecordService.LoadUsers() {
            @Override
            public void usersLoaded(List<User> user) {
                mSearchView.showUsers(user);
            }
        });
    }

    @Override
    public void loadNextUsers(String searchQuery, Long lastLoadedUserId) {
        mRecordService.getUsersFromId(searchQuery, lastLoadedUserId, new RecordService.LoadUsers() {
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
