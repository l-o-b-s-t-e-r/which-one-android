package com.android.project.homewall;

import com.android.project.model.Record;
import com.android.project.util.RecordService;
import com.android.project.util.RecordServiceImpl;

import java.util.List;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallPresenterImpl implements HomeWallPresenter.ActionListener {

    private static final String TAG = HomeWallPresenterImpl.class.getName();
    private RecordServiceImpl mRecordService;
    private HomeWallPresenter.View mHomeWallView;

    public HomeWallPresenterImpl(RecordServiceImpl recordService, HomeWallPresenter.View homeWallView) {
        mRecordService = recordService;
        mHomeWallView = homeWallView;

    }

    @Override
    public void loadLastRecords(String userName) {
        mRecordService.getLastUserRecords(userName, new RecordService.LoadLastUserRecordsCallback() {
            @Override
            public void onLastUserRecordsLoaded(List<Record> records) {
                mHomeWallView.updateRecords(records);
            }
        });
    }

    @Override
    public void loadNextRecords(String userName, Long lastLoadedRecordId) {
        mRecordService.getNextUserRecords(userName, lastLoadedRecordId, new RecordService.LoadNextUserRecordsCallback() {
            @Override
            public void onNextUserRecordsLoaded(List<Record> records) {
                mHomeWallView.updateRecords(records);
            }
        });
    }

    @Override
    public void loadRecordDetail(Long recordId) {
        mHomeWallView.openRecordDetail(recordId);
    }
}
