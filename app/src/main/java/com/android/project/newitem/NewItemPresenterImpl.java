package com.android.project.newitem;

import com.android.project.cofig.DaggerMainComponent;
import com.android.project.util.RecordService;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Lobster on 23.07.16.
 */

public class NewItemPresenterImpl implements NewItemPresenter.ActionListener {

    private static final String TAG = NewItemPresenterImpl.class.getName();
    @Inject
    public RecordService recordService;
    private NewItemPresenter.View mNewItemView;

    public NewItemPresenterImpl(NewItemPresenter.View newItemView) {
        mNewItemView = newItemView;
        DaggerMainComponent.create().inject(this);

    }

    @Override
    public void sendRecord(List<File> images, List<String> options, String name) {
        recordService.addRecord(images, options, name, new RecordService.NewRecord() {
            @Override
            public void newRecordLoaded() {
                mNewItemView.loadMainActivity();
            }
        });
    }

}
