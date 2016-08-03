package com.android.project.wall;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.android.project.R;
import com.android.project.model.Image;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.RecordServiceImpl;
import com.android.project.util.RecordService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
