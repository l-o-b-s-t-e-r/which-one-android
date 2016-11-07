package com.android.project.view.homewall;

import android.util.Log;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallPresenterImpl implements HomeWallPresenter.ActionListener {

    private static final String TAG = HomeWallPresenterImpl.class.getSimpleName();

    private HomeWallPresenter.View mHomeWallView;
    private RequestService mRequestService;
    private DatabaseManager mDatabaseManager;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public HomeWallPresenterImpl(RequestService requestService, DatabaseManager databaseManager, HomeWallPresenter.View homeWallView) {
        mRequestService = requestService;
        mDatabaseManager = databaseManager;
        mHomeWallView = homeWallView;
    }

    @Override
    public void loadLastRecords(String requestedUsername, String targetUsername) {
        Log.i(TAG, "loadLastRecords: requestedUsername - " + requestedUsername);

        Subscription subscription =
                mRequestService
                        .getLastUserRecords(requestedUsername, targetUsername)
                        .doOnSubscribe(mHomeWallView::showProgress)
                        .doOnUnsubscribe(mHomeWallView::hideProgress)
                        .flatMap(mDatabaseManager::saveAll)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mHomeWallView::updateRecords,
                                Throwable::printStackTrace

                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextRecords(String requestedUsername, Long lastLoadedRecordId, String targetUsername) {
        Log.i(TAG, String.format("loadNextRecords: username - %s, lastLoadedRecordId - %d", requestedUsername, lastLoadedRecordId));

        Subscription subscription =
                mRequestService
                        .getNextUserRecords(requestedUsername, lastLoadedRecordId, targetUsername)
                        .doOnSubscribe(mHomeWallView::showProgress)
                        .doOnUnsubscribe(mHomeWallView::hideProgress)
                        .flatMap(mDatabaseManager::saveAll)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mHomeWallView::updateRecords,
                                Throwable::printStackTrace

                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadRecordDetail(Long recordId) {
        mHomeWallView.openRecordDetail(recordId);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
