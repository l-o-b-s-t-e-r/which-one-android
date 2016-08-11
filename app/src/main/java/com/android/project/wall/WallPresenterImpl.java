package com.android.project.wall;

import com.android.project.cofig.DaggerMainComponent;
import com.android.project.model.Record;
import com.android.project.util.RecordService;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Lobster on 18.06.16.
 */

public class WallPresenterImpl implements WallPresenter.ActionListener{

    private static final String TAG = WallPresenterImpl.class.getName();
    @Inject
    public RecordService recordService;
    private WallPresenter.View mWallView;

    public WallPresenterImpl(WallPresenter.View wallView) {
        mWallView = wallView;
        DaggerMainComponent.create().inject(this);
    }

    @Override
    public void loadRecord(Long recordId) {
        recordService.getRecordById(recordId, new RecordService.LoadRecord() {
            @Override
            public void recordLoaded(Record record) {
                mWallView.updateRecord(record);
            }
        });
    }

    @Override
    public void loadLastRecords() {
        recordService.getLastRecords(new RecordService.LoadLastRecordsCallback() {
            @Override
            public void onLastRecordsLoaded(List<Record> records) {
                mWallView.showRecords(records);
            }
        });
    }

    @Override
    public void loadNextRecords(Long lastLoadedRecordId) {
        recordService.getNextRecords(lastLoadedRecordId, new RecordService.LoadNextRecordsCallback() {
            @Override
            public void onNextRecordsLoaded(List<Record> records) {
                mWallView.showRecords(records);
            }
        });
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
    public void sendVote(String userName, Long recordId, String option) {
        recordService.sendVote(userName, recordId, option);
    }
}
