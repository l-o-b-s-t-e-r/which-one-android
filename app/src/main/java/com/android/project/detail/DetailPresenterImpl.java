package com.android.project.detail;

import com.android.project.cofig.DaggerMainComponent;
import com.android.project.model.Record;
import com.android.project.util.RecordService;

import javax.inject.Inject;

/**
 * Created by Lobster on 22.06.16.
 */

public class DetailPresenterImpl implements DetailPresenter.ActionListener {

    private static final String TAG = DetailPresenterImpl.class.getName();
    @Inject
    public RecordService recordService;
    private DetailPresenter.View mDetailView;

    public DetailPresenterImpl(DetailPresenter.View detailView) {
        mDetailView = detailView;
        DaggerMainComponent.create().inject(this);

    }

    @Override
    public void loadRecord(Long recordId) {
        recordService.getRecordById(recordId, new RecordService.LoadRecord() {
            @Override
            public void recordLoaded(Record record) {
                mDetailView.showRecord(record);
            }
        });
    }

    @Override
    public void sendVote(String userName, Long recordId, String option) {
        recordService.sendVote(userName, recordId, option);
    }
}
