package com.android.project.view.detail;

import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.CommonPresenter;

/**
 * Created by Lobster on 22.06.16.
 */
public interface RecordDetailPresenter {

    interface View extends CommonPresenter.View {

        void showRecord(Record record);

        void updateQuiz(Record updatedRecord);

        void openUserPage(String username);
    }

    interface ActionListener extends CommonPresenter.ActionListener {

        void loadRecordFromDB(Long recordId);

        void loadRecordFromServer(Long recordId, String targetUsername);

        void sendVote(Record record, Option option, String username);

    }

}
