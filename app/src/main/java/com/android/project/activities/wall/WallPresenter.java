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

        void showRecords(List<Record> records);

        void showRecordDetail(Long recordId);

        void showUserPage(String username);

        void clearWall();
    }

    interface ActionListener extends CommonPresenter.ActionListener {

        void loadLastRecords();

        void loadNextRecords(Long lastLoadedRecordId);

        void openRecordDetail(Long recordId);

        void openUserPage(String username);

        void sendVote(Record record, Option option, String username, Subscriber<Record> quizSubscriber);

        Record getRecordById(Long recordId);
    }

}
