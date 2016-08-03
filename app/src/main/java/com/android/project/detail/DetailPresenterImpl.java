package com.android.project.detail;

import com.android.project.model.Record;
import com.android.project.util.RecordService;
import com.android.project.util.RecordServiceImpl;

/**
 * Created by Lobster on 22.06.16.
 */

public class DetailPresenterImpl implements DetailPresenter.ActionListener {

    private static final String TAG = DetailPresenterImpl.class.getName();
    private RecordServiceImpl mRecordService;
    private DetailPresenter.View mDetailView;

    public DetailPresenterImpl(RecordServiceImpl recordService, DetailPresenter.View detailView) {
        mRecordService = recordService;
        mDetailView = detailView;
    }

    @Override
    public void loadRecord(Long recordId) {
        mRecordService.getRecordById(recordId, new RecordService.LoadRecord() {
            @Override
            public void recordLoaded(Record record) {
                mDetailView.showRecord(record);
            }
        });
    }

    @Override
    public void sendVote(String userName, Long recordId, String option) {
        mRecordService.sendVote(userName, recordId, option);
    }
}
