package com.android.project.search;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.WhichOneApp;
import com.android.project.model.User;
import com.android.project.util.RequestService;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 01.08.16.
 */

public class SearchPresenterImpl implements SearchPresenter.ActionListener {

    private static final String TAG = SearchPresenterImpl.class.getName();
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
                                mSearchView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<User> users) {
                                mSearchView.showUsers(users);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextUsers(String searchQuery, Long lastLoadedUserId) {
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
                                mSearchView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<User> users) {
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
