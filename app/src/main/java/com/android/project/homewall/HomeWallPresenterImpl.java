package com.android.project.homewall;

import com.android.project.cofig.DaggerMainComponent;
import com.android.project.model.Record;
import com.android.project.util.RecordService;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallPresenterImpl implements HomeWallPresenter.ActionListener {

    private static final String TAG = HomeWallPresenterImpl.class.getName();
    @Inject
    public RecordService recordService;
    private HomeWallPresenter.View mHomeWallView;

    public HomeWallPresenterImpl(HomeWallPresenter.View homeWallView) {
        mHomeWallView = homeWallView;
        DaggerMainComponent.create().inject(this);

    }

    @Override
    public void loadLastRecords(String userName) {
        recordService.getLastUserRecords(userName, new RecordService.LoadLastUserRecordsCallback() {
            @Override
            public void onLastUserRecordsLoaded(List<Record> records) {
                mHomeWallView.updateRecords(records);
            }
        });
    }

    @Override
    public void loadNextRecords(String userName, Long lastLoadedRecordId) {
        recordService.getNextUserRecords(userName, lastLoadedRecordId, new RecordService.LoadNextUserRecordsCallback() {
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
