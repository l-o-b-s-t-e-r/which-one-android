package com.android.project.database;

import com.android.project.model.Record;
import com.android.project.model.User;

import java.util.List;

import rx.Observable;

/**
 * Created by Lobster on 15.09.16.
 */
public interface DatabaseManager {

    Observable<User> save(User user);

    Observable<Record> getRecordById(Long id);

    Observable<User> getUserByName(String username);

    Observable<List<Record>> saveAll(List<Record> records);

    Observable<Record> update(Record record);

    Observable<Object> clearAll();
}
