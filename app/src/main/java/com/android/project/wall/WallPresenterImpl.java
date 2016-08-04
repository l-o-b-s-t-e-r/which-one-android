package com.android.project.wall;

import com.android.project.model.Record;
import com.android.project.util.RecordService;
import com.android.project.util.RecordServiceImpl;

import java.util.List;

/**
 * Created by Lobster on 18.06.16.
 */

public class WallPresenterImpl implements WallPresenter.ActionListener{

    private static final String TAG = WallPresenterImpl.class.getName();
    private RecordServiceImpl mRecordService;
    private WallPresenter.View mWallView;

    public WallPresenterImpl(RecordServiceImpl recordService, WallPresenter.View wallView) {
        mRecordService = recordService;
        mWallView = wallView;
    }

    @Override
    public void loadRecord(Long recordId) {
        mRecordService.getRecordById(recordId, new RecordService.LoadRecord() {
            @Override
            public void recordLoaded(Record record) {
                mWallView.updateRecord(record);
            }
        });
    }

    @Override
    public void loadLastRecords() {
        mRecordService.getLastRecords(new RecordService.LoadLastRecordsCallback() {
            @Override
            public void onLastRecordsLoaded(List<Record> records) {
                mWallView.showRecords(records);
            }
        });
    }

    @Override
    public void loadNextRecords(Long lastLoadedRecordId) {
        mRecordService.getNextRecords(lastLoadedRecordId, new RecordService.LoadNextRecordsCallback() {
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
        mRecordService.sendVote(userName, recordId, option);
    }
}
