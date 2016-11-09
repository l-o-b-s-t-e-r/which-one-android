package com.android.project.view.detail;

import android.util.Log;

import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.Option;
import com.android.project.model.Record;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 22.06.16.
 */

public class RecordDetailPresenterImpl implements RecordDetailPresenter.ActionListener {

    private static final String TAG = RecordDetailPresenterImpl.class.getSimpleName();
    public RequestService mRequestService;
    public DatabaseManager mDatabaseManager;
    private RecordDetailPresenter.View mDetailView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public RecordDetailPresenterImpl(RequestService requestService, DatabaseManager databaseManager, RecordDetailPresenter.View detailView) {
        mRequestService = requestService;
        mDatabaseManager = databaseManager;
        mDetailView = detailView;
    }

    @Override
    public void loadRecordFromDB(Long recordId) {
        Log.i(TAG, "loadRecordFromDB: recordId - " + recordId);

        Subscription subscription =
                mDatabaseManager.getRecordById(recordId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mDetailView::showRecord);

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadRecordFromServer(Long recordId, String targetUsername) {
        Log.i(TAG, "loadRecordFromServer: recordId - " + recordId);

        Subscription subscription =
                mRequestService.getRecord(recordId, targetUsername)
                        .doOnUnsubscribe(mDetailView::hideProgress)
                        .flatMap(mDatabaseManager::update)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mDetailView::updateQuiz,
                                mDetailView::onError
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void sendVote(final Record record, final Option option, final String username) {
        Log.i(TAG, String.format("sendVote: recordId - %d, option - %s, username - %s", record.getRecordId(), option.getOptionName(), username));

        Subscription subscription =
                mRequestService.sendVote(record.getRecordId(), option.getOptionName(), username)
                        .flatMap(mDatabaseManager::update)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mDetailView::updateQuiz,
                                mDetailView::onError
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
