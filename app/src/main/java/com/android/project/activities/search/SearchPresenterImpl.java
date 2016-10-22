package com.android.project.activities.search;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.User;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 01.08.16.
 */

public class SearchPresenterImpl implements SearchPresenter.ActionListener {

    private static final String TAG = SearchPresenterImpl.class.getSimpleName();

    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;
    @Inject
    public CompositeSubscription compositeSubscription;

    private SearchPresenter.View mSearchView;

    public SearchPresenterImpl(SearchPresenter.View searchView) {
        mSearchView = searchView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public void loadUsers(String searchQuery) {
        Log.i(TAG, "loadUsers: searchQuery - " + searchQuery);

        mSearchView.showProgress();
        Subscription subscription =
                requestService
                        .getUsers(searchQuery)
                        .subscribe(new Subscriber<List<User>>() {
                            @Override
                            public void onCompleted() {
                                mSearchView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "loadUsers: " + e.getMessage());
                                mSearchView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<User> users) {
                                Log.i(TAG, "loadUsers: loaded, users - " + users.toString());
                                mSearchView.showUsers(users);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextUsers(String searchQuery, Long lastLoadedUserId) {
        Log.i(TAG, String.format("loadNextUsers: searchQuery - %s, lastLoadedUserId - %d", searchQuery, lastLoadedUserId));

        mSearchView.showProgress();
        Subscription subscription =
                requestService
                        .getUsersFromId(searchQuery, lastLoadedUserId)
                        .subscribe(new Subscriber<List<User>>() {
                            @Override
                            public void onCompleted() {
                                mSearchView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "loadNextUsers: " + e.getMessage());
                                mSearchView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<User> users) {
                                Log.i(TAG, "loadNextUsers: loaded, users - " + users.toString());
                                mSearchView.showUsers(users);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadUserPage(User user) {
        mSearchView.showUserPage(user);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
