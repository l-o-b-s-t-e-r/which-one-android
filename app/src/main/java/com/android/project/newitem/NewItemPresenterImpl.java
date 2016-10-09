package com.android.project.newitem;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.MainComponent;
import com.android.project.util.RequestService;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Lobster on 23.07.16.
 */

public class NewItemPresenterImpl implements NewItemPresenter.ActionListener {

    private static final String TAG = NewItemPresenterImpl.class.getName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;

    private NewItemPresenter.View mNewItemView;

    public NewItemPresenterImpl(NewItemPresenter.View newItemView, MainComponent mainComponent) {
        mNewItemView = newItemView;
        mainComponent.inject(this);
    }

    @Override
    public void sendRecord(List<File> images, List<String> options, String name, String title) {
        requestService
                .addRecord(images, options, name, title)
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
                        mNewItemView.loadMainActivity();
                    }
                });
    }

}
