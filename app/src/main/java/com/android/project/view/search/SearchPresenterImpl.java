package com.android.project.view.search;

import android.util.Log;

import com.android.project.api.RequestService;
import com.android.project.model.User;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 01.08.16.
 */

public class SearchPresenterImpl implements SearchPresenter.ActionListener {

    private static final String TAG = SearchPresenterImpl.class.getSimpleName();
    public RequestService mRequestService;
    private SearchPresenter.View mSearchView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public SearchPresenterImpl(RequestService requestService, SearchPresenter.View searchView) {
        mRequestService = requestService;
        mSearchView = searchView;
    }

    @Override
    public void loadUsers(String searchQuery) {
        Log.i(TAG, "loadUsers: searchQuery - " + searchQuery);

        Subscription subscription =
                mRequestService
                        .getUsers(searchQuery)
                        .doOnSubscribe(mSearchView::showProgress)
                        .doOnUnsubscribe(mSearchView::hideProgress)
                        .subscribe(
                                mSearchView::showUsers,
                                mSearchView::onError

                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextUsers(String searchQuery, String lastLoadedUsername) {
        Log.i(TAG, String.format("loadNextUsers: searchQuery - %s, lastLoadedUsername - %s", searchQuery, lastLoadedUsername));

        Subscription subscription =
                mRequestService
                        .getUsersFromUsername(searchQuery, lastLoadedUsername)
                        .doOnSubscribe(mSearchView::showProgress)
                        .doOnUnsubscribe(mSearchView::hideProgress)
                        .subscribe(
                                mSearchView::showUsers,
                                mSearchView::onError

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
