package com.android.project.activities.newrecord;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 23.07.16.
 */

public class NewRecordPresenterImpl implements NewRecordPresenter.ActionListener {

    private static final String TAG = NewRecordPresenterImpl.class.getSimpleName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;
    @Inject
    public CompositeSubscription compositeSubscription;

    private NewRecordPresenter.View mNewRecordView;

    public NewRecordPresenterImpl(NewRecordPresenter.View newItemView) {
        mNewRecordView = newItemView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public void sendRecord(List<File> images, List<String> options, String username, String title) {
        Log.i(TAG, String.format("sendRecord: images - %s, options - %s, username - %s, title - %s", images.toString(), options.toString(), username, title));

        mNewRecordView.showProgress();
        Subscription subscription =
                requestService
                        .addRecord(images, options, username, title)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {
                                mNewRecordView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "sendRecord: " + e.getMessage());
                                mNewRecordView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                Log.i(TAG, "sendRecord: sent (SUCCESS)");
                                mNewRecordView.loadMainActivity();
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
