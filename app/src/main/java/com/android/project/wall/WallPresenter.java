package com.android.project.wall;

import com.android.project.model.Option;
import com.android.project.model.Record;

import java.util.List;

/**
 * Created by Lobster on 18.06.16.
 */

public interface WallPresenter {

    interface View {

        void showRecords(List<Long> recordIds);

        void showRecordDetail(Long recordId);

        void showUserPage(String userName);

    }

    interface ActionListener{

        Record getRecordById(Long recordId);

        void loadLastRecords();

        void loadNextRecords(Long lastLoadedRecordId);

        void openRecordDetail(Long recordId);

        void openUserPage(String userName);

        void sendVote(Long recordId, Option option, String userName);

    }

}
