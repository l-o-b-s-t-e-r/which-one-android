package com.android.project.wall;

import com.android.project.model.Record;

import java.util.List;

/**
 * Created by Lobster on 18.06.16.
 */

public interface WallPresenter {

    interface View {

        void updateRecord(Record record);

        void showRecords(List<Record> records);

        void showRecordDetail(Long recordId);

        void showUserPage(String userName);

    }

    interface ActionListener{

        void loadRecord(Long recordId);

        void loadLastRecords();

        void loadNextRecords(Long lastLoadedRecordId);

        void openRecordDetail(Long recordId);

        void openUserPage(String userName);

        void sendVote(String userName, Long recordId, String option);

    }

}
