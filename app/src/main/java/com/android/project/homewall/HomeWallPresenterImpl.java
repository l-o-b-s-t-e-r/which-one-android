package com.android.project.homewall;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.MainComponent;
import com.android.project.model.Record;
import com.android.project.util.RequestService;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Lobster on 29.07.16.
 */

public class HomeWallPresenterImpl implements HomeWallPresenter.ActionListener {

    private static final String TAG = HomeWallPresenterImpl.class.getName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;

    private HomeWallPresenter.View mHomeWallView;

    public HomeWallPresenterImpl(HomeWallPresenter.View homeWallView, MainComponent mainComponent) {
        mHomeWallView = homeWallView;
        mainComponent.inject(this);
    }

    @Override
    public Record getRecordById(Long recordId) {
        return databaseManager.getById(recordId);
    }

    @Override
    public void loadLastRecords(String userName) {
        requestService.getLastUserRecords(userName, new RequestService.LoadLastUserRecordsCallback() {
            @Override
            public void onLastUserRecordsLoaded(List<Record> records) {
                List<Long> recordIds = databaseManager.saveAll(records);
                mHomeWallView.updateRecords(recordIds);
            }
        });
    }

    @Override
    public void loadNextRecords(String userName, Long lastLoadedRecordId) {
        requestService.getNextUserRecords(userName, lastLoadedRecordId, new RequestService.LoadNextUserRecordsCallback() {
            @Override
            public void onNextUserRecordsLoaded(List<Record> records) {
                List<Long> recordIds = databaseManager.saveAll(records);
                mHomeWallView.updateRecords(recordIds);
            }
        });
    }

    @Override
    public void loadRecordDetail(Long recordId) {
        mHomeWallView.openRecordDetail(recordId);
    }
}
