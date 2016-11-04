package com.android.project.activities.search;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.User;

import javax.inject.Inject;

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

        Subscription subscription =
                requestService
                        .getUsers(searchQuery)
                        .doOnSubscribe(mSearchView::showProgress)
                        .doOnUnsubscribe(mSearchView::hideProgress)
                        .subscribe(
                                mSearchView::showUsers,
                                Throwable::printStackTrace

                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextUsers(String searchQuery, String lastLoadedUsername) {
        Log.i(TAG, String.format("loadNextUsers: searchQuery - %s, lastLoadedUsername - %s", searchQuery, lastLoadedUsername));

        Subscription subscription =
                requestService
                        .getUsersFromUsername(searchQuery, lastLoadedUsername)
                        .doOnSubscribe(mSearchView::showProgress)
                        .doOnUnsubscribe(mSearchView::hideProgress)
                        .subscribe(
                                mSearchView::showUsers,
                                Throwable::printStackTrace

                        );

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
