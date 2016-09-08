package com.android.project.util;

import com.android.project.model.Record;
import com.android.project.model.User;

import java.io.File;
import java.util.List;

/**
 * Created by Lobster on 19.06.16.
 */
public interface RecordService {

    void signUp(String name, String password, String email);

    void checkName(String name, Checking callback);

    void checkEmail(String email, Checking callback);

    void remindInfo(String email, Checking callback);

    void signIn(String name, String password, Checking callback);

    void getNextRecords(Long recordId, LoadNextRecordsCallback callback);

    void getLastRecords(LoadLastRecordsCallback callback);

    void getLastUserRecords(String userName, LoadLastUserRecordsCallback callback);

    void getNextUserRecords(String userName, Long recordId, LoadNextUserRecordsCallback callback);

    void getRecordById(Long recordId, LoadRecord callback);

    void getUserInfo(String name, LoadUserInfo callback);

    void updateBackground(File background, String name, LoadUserInfo callback);

    void updateAvatar(File background, String name, LoadUserInfo callback);

    void addRecord(List<File> files, List<String> options, String name, NewRecord callback);

    void getUsers(String searchQuery, LoadUsers callback);

    void getUsersFromId(String searchQuery, Long userId, LoadUsers callback);

    void sendVote(String userName, Long recordId, String option);

    interface Checking {
        void checkResult(Integer requestCode);
    }

    interface LoadNextRecordsCallback {
        void onNextRecordsLoaded(List<Record> records);
    }

    interface LoadLastRecordsCallback {
        void onLastRecordsLoaded(List<Record> records);
    }

    interface LoadLastUserRecordsCallback {
        void onLastUserRecordsLoaded(List<Record> records);
    }

    interface LoadNextUserRecordsCallback {
        void onNextUserRecordsLoaded(List<Record> records);
    }

    interface LoadRecord {
        void recordLoaded(Record record);
    }

    interface LoadUserInfo {
        void onUserInfoLoaded(User user);
    }

    interface NewRecord {
        void newRecordLoaded();
    }

    interface LoadUsers {
        void usersLoaded(List<User> user);
    }
}
