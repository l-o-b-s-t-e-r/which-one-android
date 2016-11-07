package com.android.project.view.newrecord;

import android.util.Log;

import com.android.project.api.RequestService;
import com.android.project.util.ImageManager;

import java.io.File;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 23.07.16.
 */

public class NewRecordPresenterImpl implements NewRecordPresenter.ActionListener {

    private static final String TAG = NewRecordPresenterImpl.class.getSimpleName();

    private RequestService mRequestService;
    private NewRecordPresenter.View mNewRecordView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public NewRecordPresenterImpl(RequestService requestService, NewRecordPresenter.View newItemView) {
        mRequestService = requestService;
        mNewRecordView = newItemView;
    }

    @Override
    public void loadImage(File imageFile) {
        Log.i(TAG, "loadImage: " + imageFile.getAbsolutePath());

        Subscription subscription =
                ImageManager.getInstance().resizeImage(imageFile)
                        .doOnSubscribe(mNewRecordView::showProgress)
                        .doOnUnsubscribe(mNewRecordView::hideProgress)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                mNewRecordView::showImage,
                                mNewRecordView::onError
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void sendRecord(List<File> images, List<String> options, String username, String title) {
        Log.i(TAG, String.format("sendRecord: images - %s, options - %s, username - %s, title - %s", images.toString(), options.toString(), username, title));

        Subscription subscription =
                mRequestService
                        .addRecord(images, options, username, title)
                        .doOnSubscribe(mNewRecordView::showProgress)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                aVoid -> mNewRecordView.loadMainActivity(),
                                throwable -> {
                                    mNewRecordView.onError(throwable);
                                    mNewRecordView.hideProgress();
                                }
                        );

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
