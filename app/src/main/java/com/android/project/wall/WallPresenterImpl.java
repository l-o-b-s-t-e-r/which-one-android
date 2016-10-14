package com.android.project.wall;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.WhichOneApp;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.RequestService;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 18.06.16.
 */

public class WallPresenterImpl implements WallPresenter.ActionListener {

    private static final String TAG = WallPresenterImpl.class.getName();
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
        return databaseManager.getById(recordId);
    }

    @Override
    public void loadLastRecords() {
        mWallView.showProgress();
        Subscription subscription =
                requestService
                        .getLastRecords()
                        .subscribe(new Subscriber<List<Record>>() {
                            @Override
                            public void onCompleted() {
                                mWallView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mWallView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<Record> records) {
                                mWallView.clearWall();

                                List<Long> recordIds = databaseManager.saveAll(records); //use Rx
                                mWallView.showRecords(recordIds);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextRecords(Long lastLoadedRecordId) {
        mWallView.showProgress();
        Subscription subscription =
                requestService
                        .getNextRecords(lastLoadedRecordId)
                        .subscribe(new Subscriber<List<Record>>() {
                            @Override
                            public void onCompleted() {
                                mWallView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mWallView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<Record> records) {
                                List<Long> recordIds = databaseManager.saveAll(records); //use Rx
                                mWallView.showRecords(recordIds);
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
