package com.android.project.activities.newrecord;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.util.ImageManager;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
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
    public void loadImage(File imageFile) {
        Log.i(TAG, "loadImage: " + imageFile.getAbsolutePath());

        mNewRecordView.showProgress();
        Subscription subscription =
                resizeImage(imageFile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<File>() {
                            @Override
                            public void onCompleted() {
                                mNewRecordView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "loadImage: " + e.getMessage());
                                mNewRecordView.hideProgress();
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(File file) {
                                Log.i(TAG, "loadImage: SUCCESS");
                                mNewRecordView.showImage(file);
                            }
                        });

        compositeSubscription.add(subscription);
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

    private Observable<File> resizeImage(final File imageFile) {
        return Observable.defer(new Func0<Observable<File>>() {
            @Override
            public Observable<File> call() {
                return Observable.just(ImageManager.getInstance().resizeImage(imageFile));
            }
        });
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
