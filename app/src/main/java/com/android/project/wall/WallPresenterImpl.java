package com.android.project.wall;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.MainComponent;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.RequestService;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

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
        requestService
                .getLastRecords()
                .subscribe(new Subscriber<List<Record>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Record> records) {
                        List<Long> recordIds = databaseManager.saveAll(records); //use Rx
                        mWallView.showRecords(recordIds);
                    }
                });
    }

    @Override
    public void loadNextRecords(Long lastLoadedRecordId) {
        requestService
                .getNextRecords(lastLoadedRecordId)
                .subscribe(new Subscriber<List<Record>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Record> records) {
                        List<Long> recordIds = databaseManager.saveAll(records); //use Rx
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
    public void sendVote(final Long recordId, final Option option, final String userName) {
        requestService
                .sendVote(recordId, option.getOptionName(), userName)
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
                        databaseManager.addVote(recordId, option, userName);
                    }
                });
    }
}
