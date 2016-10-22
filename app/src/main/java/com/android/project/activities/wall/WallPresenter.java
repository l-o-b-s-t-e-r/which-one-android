package com.android.project.activities.wall;

import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.CommonPresenter;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Lobster on 18.06.16.
 */

public interface WallPresenter {

    interface View extends CommonPresenter.View {

        void showRecords(List<Long> recordIds);

        void showRecordDetail(Long recordId);

        void showUserPage(String userName);

        void clearWall();
    }

    interface ActionListener extends CommonPresenter.ActionListener {

        Record loadRecordById(Long recordId);

        void loadLastRecords();

        void loadNextRecords(Long lastLoadedRecordId);

        void openRecordDetail(Long recordId);

        void openUserPage(String userName);

        void sendVote(Long recordId, Option option, String userName, List<Subscriber<Void>> subscribers);

    }

}
