package com.android.project.wall;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.MainComponent;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.RequestService;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Lobster on 18.06.16.
 */

public class WallPresenterImpl implements WallPresenter.ActionListener{

    private static final String TAG = WallPresenterImpl.class.getName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;

    private WallPresenter.View mWallView;

    public WallPresenterImpl(WallPresenter.View wallView, MainComponent mainComponent) {
        mWallView = wallView;
        mainComponent.inject(this);
    }

    @Override
    public Record getRecordById(Long recordId) {
        return databaseManager.getById(recordId);
    }

    @Override
    public void loadLastRecords() {
        requestService.getLastRecords(new RequestService.LoadLastRecordsCallback() {
            @Override
            public void onLastRecordsLoaded(List<Record> records) {
                List<Long> recordIds = databaseManager.saveAll(records);
                mWallView.showRecords(recordIds);
            }
        });
    }

    @Override
    public void loadNextRecords(Long lastLoadedRecordId) {
        requestService.getNextRecords(lastLoadedRecordId, new RequestService.LoadNextRecordsCallback() {
            @Override
            public void onNextRecordsLoaded(List<Record> records) {
                List<Long> recordIds = databaseManager.saveAll(records);
                mWallView.showRecords(recordIds);
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
    public void sendVote(Long recordId, Option option, String userName) {
        requestService.sendVote(recordId, option, userName, new RequestService.NewVote() {
            @Override
            public void voteSent(Long recordId, Option option, String userName) {
                databaseManager.addVote(recordId, option, userName);
            }
        });
    }
}
