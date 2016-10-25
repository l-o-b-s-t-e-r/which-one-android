package com.android.project.activities.wall;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.Option;
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
 * Created by Lobster on 18.06.16.
 */

public class WallPresenterImpl implements WallPresenter.ActionListener {

    private static final String TAG = WallPresenterImpl.class.getSimpleName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;
    @Inject
    public CompositeSubscription compositeSubscription;

    private WallPresenter.View mWallView;

    public WallPresenterImpl(WallPresenter.View wallView) {
        mWallView = wallView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public Record getRecordById(Long recordId) {
        return databaseManager.getRecordById(recordId);
    }

    @Override
    public void loadLastRecords() {
        Log.i(TAG, "loadLastRecords: start loading");

        mWallView.showProgress();
        Subscription subscription =
                requestService
                        .getLastRecords()
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
                                mWallView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "loadLastRecords: " + e.getMessage());
                                mWallView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<Record> records) {
                                Log.i(TAG, "loadLastRecords: records have been mapped");
                                mWallView.showRecords(records);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextRecords(Long lastLoadedRecordId) {
        Log.i(TAG, "loadNextRecords: start loading");

        mWallView.showProgress();
        Subscription subscription =
                requestService
                        .getNextRecords(lastLoadedRecordId)
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
                                mWallView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "loadNextRecords: " + e.getMessage());
                                mWallView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<Record> records) {
                                Log.i(TAG, "loadNextRecords: records have been mapped");
                                mWallView.showRecords(records);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void openRecordDetail(Long recordId) {
        mWallView.showRecordDetail(recordId);
    }

    @Override
    public void openUserPage(String userName) {
        mWallView.showUserPage(userName);
    }

    @Override
    public void sendVote(final Record record, final Option option, final String username, final Subscriber<Record> quizSubscriber) {
        Log.i(TAG, String.format("sendVote: recordId - %d, option - %s, username - %s", record.getRecordId(), option.getOptionName(), username));

        Subscription subscription =
                requestService.sendVote(record.getRecordId(), option.getOptionName(), username)
                        .flatMap(new Func1<Void, Observable<Option>>() {
                            @Override
                            public Observable<Option> call(Void aVoid) {
                                return Observable.just(databaseManager.addVote(record.getRecordId(), option, username));
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Option>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "sendVote: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Option option) {
                                Log.i(TAG, "sendVote: SUCCESS");
                                addUserVote(option, username);

                                if (!quizSubscriber.isUnsubscribed()) {
                                    Observable.just(record)
                                            .subscribe(quizSubscriber);
                                }
                            }
                        });

        compositeSubscription.add(subscription);
    }

    private void addUserVote(Option option, String username) {
        option.getVotes().add(username);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
