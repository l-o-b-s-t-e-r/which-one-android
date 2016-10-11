package com.android.project.homewall;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.WhichOneApp;
import com.android.project.model.Record;
import com.android.project.util.RequestService;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallPresenterImpl implements HomeWallPresenter.ActionListener {

    private static final String TAG = HomeWallPresenterImpl.class.getName();
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
        return databaseManager.getById(recordId);
    }

    @Override
    public void loadLastRecords(String userName) {
        Subscription subscription =
                requestService
                .getLastUserRecords(userName)
                .subscribe(new Subscriber<List<Record>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Record> records) {
                        List<Long> recordIds = databaseManager.saveAll(records); //use Rx
                        mHomeWallView.updateRecords(recordIds);
                    }
                });

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadNextRecords(String userName, Long lastLoadedRecordId) {
        Subscription subscription =
                requestService
                .getNextUserRecords(userName, lastLoadedRecordId)
                .subscribe(new Subscriber<List<Record>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Record> records) {
                        List<Long> recordIds = databaseManager.saveAll(records); //use Rx
                        mHomeWallView.updateRecords(recordIds);
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
