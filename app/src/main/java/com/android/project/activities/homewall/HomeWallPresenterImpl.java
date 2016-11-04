package com.android.project.activities.homewall;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallPresenterImpl implements HomeWallPresenter.ActionListener {

    private static final String TAG = HomeWallPresenterImpl.class.getSimpleName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;
    @Inject
    public CompositeSubscription compositeSubscription;

    private String mTargetUsername;
    private HomeWallPresenter.View mHomeWallView;

    public HomeWallPresenterImpl(String targetUsername, HomeWallPresenter.View homeWallView) {
        mTargetUsername = targetUsername;
        mHomeWallView = homeWallView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public void loadLastRecords(String requestedUsername) {
        Log.i(TAG, "loadLastRecords: username - " + requestedUsername);

        Subscription subscription =
                requestService
                        .getLastUserRecords(requestedUsername, mTargetUsername)
                        .flatMap(records -> Observable.just(databaseManager.saveAll(records)))
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(mHomeWallView::showProgress)
                        .doOnUnsubscribe(mHomeWallView::hideProgress)
                        .subscribe(
                                mHomeWallView::updateRecords,
                                Throwable::printStackTrace

                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextRecords(String requestedUsername, Long lastLoadedRecordId) {
        Log.i(TAG, String.format("loadNextRecords: username - %s, lastLoadedRecordId - %d", requestedUsername, lastLoadedRecordId));

        Subscription subscription =
                requestService
                        .getNextUserRecords(requestedUsername, lastLoadedRecordId, mTargetUsername)
                        .flatMap(records -> Observable.just(databaseManager.saveAll(records)))
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(mHomeWallView::showProgress)
                        .doOnUnsubscribe(mHomeWallView::hideProgress)
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
