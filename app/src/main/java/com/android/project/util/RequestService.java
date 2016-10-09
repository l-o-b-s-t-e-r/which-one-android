package com.android.project.util;

import com.android.project.model.Record;
import com.android.project.model.User;

import java.io.File;
import java.util.List;

import rx.Observable;

/**
 * Created by Lobster on 19.06.16.
 */
public interface RequestService {

    Observable<Void> signUp(String name, String password, String email);

    Observable<Void> checkName(String name);

    Observable<Void> checkEmail(String email);

    Observable<Void> remindInfo(String email);

    Observable<Void> signIn(String name, String password);

    Observable<List<Record>> getNextRecords(Long recordId);

    Observable<List<Record>> getLastRecords();

    Observable<List<Record>> getLastUserRecords(String userName);

    Observable<List<Record>> getNextUserRecords(String userName, Long recordId);

    Observable<User> getUserInfo(String name);

    Observable<User> updateBackground(File background, String name);

    Observable<User> updateAvatar(File background, String name);

    Observable<Void> addRecord(List<File> files, List<String> options, String name, String title);

    Observable<List<User>> getUsers(String searchQuery);

    Observable<List<User>> getUsersFromId(String searchQuery, Long userId);

    Observable<Void> sendVote(Long recordId, String optionName, String userName);
}
