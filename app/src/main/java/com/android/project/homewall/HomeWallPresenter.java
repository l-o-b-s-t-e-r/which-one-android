package com.android.project.homewall;

import com.android.project.model.Record;

import java.util.List;

/**
 * Created by Lobster on 29.07.16.
 */

public interface HomeWallPresenter {

    interface View {
        void updateRecords(List<Record> records);

        void openRecordDetail(Long recordId);
    }

    interface ActionListener {
        void loadLastRecords(String userName);

        void loadNextRecords(String userName, Long lastLoadedRecordId);

        void loadRecordDetail(Long recordId);
    }

}
