package com.android.project.activities.homewall;

import com.android.project.model.Record;
import com.android.project.util.CommonPresenter;

import java.util.List;

/**
 * Created by Lobster on 29.07.16.
 */

public interface HomeWallPresenter {

    interface View extends CommonPresenter.View {
        void updateRecords(List<Record> records);

        void openRecordDetail(Long recordId);

        void clearHomeWall();
    }

    interface ActionListener extends CommonPresenter.ActionListener {

        void loadLastRecords(String requestedUsername);

        void loadNextRecords(String requestedUsername, Long lastLoadedRecordId);

        void loadRecordDetail(Long recordId);
    }

}
