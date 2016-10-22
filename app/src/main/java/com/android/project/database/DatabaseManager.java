package com.android.project.database;

import com.android.project.model.Option;
import com.android.project.model.Record;

import java.util.List;

/**
 * Created by Lobster on 15.09.16.
 */
public interface DatabaseManager {

    Long save(Record record);

    void addVote(Long recordId, Option option, String votedUser);

    List<Long> saveAll(List<Record> records);

    Record getById(Long id);

}
