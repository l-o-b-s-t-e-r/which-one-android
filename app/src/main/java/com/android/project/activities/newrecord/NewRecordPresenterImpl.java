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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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

        Subscription subscription =
                resizeImage(imageFile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(mNewRecordView::showProgress)
                        .doOnUnsubscribe(mNewRecordView::hideProgress)
                        .subscribe(
                                mNewRecordView::showImage,
                                Throwable::printStackTrace
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void sendRecord(List<File> images, List<String> options, String username, String title) {
        Log.i(TAG, String.format("sendRecord: images - %s, options - %s, username - %s, title - %s", images.toString(), options.toString(), username, title));

        Subscription subscription =
                requestService
                        .addRecord(images, options, username, title)
                        .doOnSubscribe(mNewRecordView::showProgress)
                        .doOnUnsubscribe(mNewRecordView::hideProgress)
                        .subscribe(
                                aVoid -> mNewRecordView.loadMainActivity(),
                                Throwable::printStackTrace
                        );

        compositeSubscription.add(subscription);
    }

    private Observable<File> resizeImage(final File imageFile) {
        return Observable.defer(() -> Observable.just(ImageManager.getInstance().resizeImage(imageFile)));
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
