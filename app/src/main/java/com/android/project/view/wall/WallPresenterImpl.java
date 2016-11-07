package com.android.project.view.wall;

import android.util.Log;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.Option;
import com.android.project.model.Record;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 18.06.16.
 */

public class WallPresenterImpl implements WallPresenter.ActionListener {

    private static final String TAG = WallPresenterImpl.class.getSimpleName();

    private WallPresenter.View mWallView;
    private RequestService mRequestService;
    private DatabaseManager mDatabaseManager;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public WallPresenterImpl(RequestService requestService, DatabaseManager databaseManager, WallPresenter.View wallView) {
        mRequestService = requestService;
        mDatabaseManager = databaseManager;
        mWallView = wallView;
    }

    @Override
    public void getRecordById(Long recordId, Integer position) {
        mDatabaseManager.getRecordById(recordId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        record -> mWallView.updateRecord(record, position, true),
                        mWallView::onError
                );
    }

    @Override
    public void loadLastRecords(String username) {
        Log.i(TAG, "loadLastRecords: start loading");

        Subscription subscription =
                mRequestService
                        .getLastRecords(username)
                        .doOnSubscribe(mWallView::showProgress)
                        .doOnUnsubscribe(mWallView::hideProgress)
                        .flatMap(mDatabaseManager::saveAll)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mWallView::showRecords,
                                mWallView::onError

                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextRecords(Long lastLoadedRecordId, String username) {
        Log.i(TAG, "loadNextRecords: start loading");

        Subscription subscription =
                mRequestService
                        .getNextRecords(lastLoadedRecordId, username)
                        .doOnSubscribe(mWallView::showProgress)
                        .doOnUnsubscribe(mWallView::hideProgress)
                        .flatMap(mDatabaseManager::saveAll)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mWallView::showRecords,
                                mWallView::onError
                        );

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
    public void sendVote(final Record record, final Option option, final String username, final Subscriber<Record> quizSubscriber, final Integer position) {
        Log.i(TAG, String.format("sendVote: recordId - %d, option - %s, username - %s", record.getRecordId(), option.getOptionName(), username));

        Subscription subscription =
                mRequestService
                        .sendVote(record.getRecordId(), option.getOptionName(), username)
                        .flatMap(mDatabaseManager::update)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                newRecord -> {
                                    Log.i(TAG, "sendVote: SUCCESS");

                                    mWallView.updateRecord(newRecord, position, false);
                                    if (!quizSubscriber.isUnsubscribed()) {
                                        Observable.just(newRecord)
                                                .subscribe(quizSubscriber);
                                    }
                                },
                                mWallView::onError
                        );

        compositeSubscription.add(subscription);
    }


    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
