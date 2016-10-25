package com.android.project.activities.homewall;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.Record;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

    private HomeWallPresenter.View mHomeWallView;

    public HomeWallPresenterImpl(HomeWallPresenter.View homeWallView) {
        mHomeWallView = homeWallView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public Record getRecordById(Long recordId) {
        return databaseManager.getRecordById(recordId);
    }

    @Override
    public void loadLastRecords(String username) {
        Log.i(TAG, "loadLastRecords: username - " + username);

        mHomeWallView.showProgress();
        Subscription subscription =
                requestService
                        .getLastUserRecords(username)
                        .flatMap(new Func1<List<Record>, Observable<List<Record>>>() {
                            @Override
                            public Observable<List<Record>> call(List<Record> records) {
                                Log.i(TAG, "loadLastRecords: records have been loaded");
                                return Observable.just(databaseManager.saveAll(records));
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<Record>>() {
                            @Override
                            public void onCompleted() {
                                mHomeWallView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "loadLastRecords: " + e.getMessage());
                                mHomeWallView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<Record> records) {
                                Log.i(TAG, "loadLastRecords: records have been mapped");
                                mHomeWallView.updateRecords(records);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextRecords(String username, Long lastLoadedRecordId) {
        Log.i(TAG, String.format("loadNextRecords: username - %s, lastLoadedRecordId - %d", username, lastLoadedRecordId));

        mHomeWallView.showProgress();
        Subscription subscription =
                requestService
                        .getNextUserRecords(username, lastLoadedRecordId)
                        .flatMap(new Func1<List<Record>, Observable<List<Record>>>() {
                            @Override
                            public Observable<List<Record>> call(List<Record> records) {
                                Log.i(TAG, "loadNextRecords: records have been loaded");
                                return Observable.just(databaseManager.saveAll(records));
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<Record>>() {
                            @Override
                            public void onCompleted() {
                                mHomeWallView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "loadNextRecords: " + e.getMessage());
                                mHomeWallView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<Record> records) {
                                Log.i(TAG, "loadNextRecords: records have been mapped");
                                mHomeWallView.updateRecords(records);
                            }
                        });

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
