package com.android.project.activities.detail;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.Option;
import com.android.project.model.Record;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 22.06.16.
 */

public class RecordDetailPresenterImpl implements RecordDetailPresenter.ActionListener {

    private static final String TAG = RecordDetailPresenterImpl.class.getSimpleName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;
    @Inject
    public CompositeSubscription compositeSubscription;

    private RecordDetailPresenter.View mDetailView;

    public RecordDetailPresenterImpl(RecordDetailPresenter.View detailView) {
        mDetailView = detailView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public void loadRecord(Long recordId) {
        Log.i(TAG, "loadRecord: recordId - " + recordId);

        Subscription subscription =
                Observable.defer(() -> Observable.just(databaseManager.getRecordById(recordId)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mDetailView::showRecord);

        compositeSubscription.add(subscription);
    }

    @Override
    public void sendVote(final Record record, final Option option, final String username) {
        Log.i(TAG, String.format("sendVote: recordId - %d, option - %s, username - %s", record.getRecordId(), option.getOptionName(), username));

        Subscription subscription =
                requestService.sendVote(record.getRecordId(), option.getOptionName(), username)
                        .flatMap(newRecord -> Observable.just(databaseManager.update(newRecord)))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mDetailView::updateQuiz,
                                Throwable::printStackTrace
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
