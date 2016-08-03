package com.android.project.newitem;

import com.android.project.util.RecordService;
import com.android.project.util.RecordServiceImpl;

import java.io.File;
import java.util.List;

/**
 * Created by Lobster on 23.07.16.
 */

public class NewItemPresenterImpl implements NewItemPresenter.ActionListener {

    private static final String TAG = NewItemPresenterImpl.class.getName();
    private RecordServiceImpl mRecordService;
    private NewItemPresenter.View mNewItemView;

    public NewItemPresenterImpl(RecordServiceImpl recordService, NewItemPresenter.View newItemView) {
        mRecordService = recordService;
        mNewItemView = newItemView;
    }

    @Override
    public void sendRecord(List<File> images, List<String> options, String name) {
        mRecordService.addRecord(images, options, name, new RecordService.NewRecord() {
            @Override
            public void newRecordLoaded() {
                mNewItemView.loadMainActivity();
            }
        });
    }

}
