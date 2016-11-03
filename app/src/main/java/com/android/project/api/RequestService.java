package com.android.project.api;

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

    Observable<List<Record>> getLastRecords(String targetUsername);

    Observable<List<Record>> getNextRecords(Long recordId, String targetUsername);

    Observable<List<Record>> getLastUserRecords(String requestedUsername, String targetUsername);

    Observable<List<Record>> getNextUserRecords(String requestedUsername, Long recordId, String targetUsername);

    Observable<User> getUserInfo(String name);

    Observable<User> updateBackground(File background, String name);

    Observable<User> updateAvatar(File avatar, String name);

    Observable<Void> addRecord(List<File> files, List<String> options, String name, String description);

    Observable<List<User>> getUsers(String searchQuery);

    Observable<List<User>> getUsersFromUsername(String searchQuery, String lastUsername);

    Observable<Record> sendVote(Long recordId, String optionName, String userName);
}
