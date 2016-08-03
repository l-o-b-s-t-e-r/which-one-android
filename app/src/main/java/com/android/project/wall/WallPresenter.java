package com.android.project.wall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.android.project.adapter.RecordRecyclerViewAdapter;
import com.android.project.model.Image;
import com.android.project.model.Record;

import java.io.File;
import java.util.List;

/**
 * Created by Lobster on 18.06.16.
 */

public interface WallPresenter {

    interface View {

        void showRecords(List<Record> records);

        void showRecordDetail(Long recordId);

        void showUserPage(String userName);

    }

    interface ActionListener{

        void loadLastRecords();

        void loadNextRecords(Long lastLoadedRecordId);

        void openRecordDetail(Long recordId);

        void openUserPage(String userName);

        void sendVote(String userName, Long recordId, String option);

    }

}
