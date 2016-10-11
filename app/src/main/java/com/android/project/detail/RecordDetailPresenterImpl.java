package com.android.project.detail;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.WhichOneApp;
import com.android.project.model.Option;
import com.android.project.util.RequestService;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 22.06.16.
 */

public class RecordDetailPresenterImpl implements RecordDetailPresenter.ActionListener {

    private static final String TAG = RecordDetailPresenterImpl.class.getName();
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
        mDetailView.showRecord(databaseManager.getById(recordId));
    }

    @Override
    public void sendVote(final Long recordId, final Option option, final String userName) {
        Subscription subscription =
                requestService
                        .sendVote(recordId, option.getOptionName(), userName)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                databaseManager.addVote(recordId, option, userName);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
