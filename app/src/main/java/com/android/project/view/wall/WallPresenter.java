package com.android.project.view.wall;

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

        void updateRecord(Record record, Integer position, Boolean notify);

        void clearWall();
    }

    interface ActionListener extends CommonPresenter.ActionListener {

        void loadLastRecords(String username);

        void loadNextRecords(Long lastLoadedRecordId, String username);

        void openRecordDetail(Long recordId);

        void openUserPage(String username);

        void sendVote(Record record, Option option, String username, Subscriber<Record> quizSubscriber, Integer position);

        void getRecordById(Long recordId, Integer position);
    }

}
