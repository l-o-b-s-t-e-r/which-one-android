package com.android.project.detail;

import com.android.project.model.Option;
import com.android.project.model.Record;

/**
 * Created by Lobster on 22.06.16.
 */
public interface DetailPresenter {

    interface View {

        void showRecord(Record record);

    }

    interface ActionListener {

        void loadRecord(Long recordId);

        void sendVote(Long recordId, Option option, String userName);

    }

}
