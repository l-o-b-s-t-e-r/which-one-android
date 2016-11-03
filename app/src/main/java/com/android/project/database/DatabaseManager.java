package com.android.project.database;

import com.android.project.model.Record;

import java.util.List;

/**
 * Created by Lobster on 15.09.16.
 */
public interface DatabaseManager {

    Record save(Record record);

    Record update(Record record);

    void delete(Integer recordId);

    List<Record> saveAll(List<Record> records);

    Record getRecordById(Long id);

}
