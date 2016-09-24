package com.android.project.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by Lobster on 14.09.16.
 */

public class OptionEntity {

    public static final String OPTION_ID_FIELD_NAME = "id";
    public static final String RECORD_ID_FIELD_NAME = "record_id";
    public static final String OPTION_NAME_FIELD_NAME = "option_name";
    public static final String VOTES_FIELD_NAME = "votes";

    @DatabaseField(generatedId = true, columnName = OPTION_ID_FIELD_NAME)
    private Integer id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = RECORD_ID_FIELD_NAME)
    private RecordEntity mRecord;
    @DatabaseField(columnName = OPTION_NAME_FIELD_NAME)
    private String mOptionName;
    @ForeignCollectionField(columnName = VOTES_FIELD_NAME, eager = true)
    private ForeignCollection<VoteEntity> mVotes;

    public OptionEntity() {

    }

    public OptionEntity(RecordEntity record, String optionName) {
        mRecord = record;
        mOptionName = optionName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RecordEntity getRecord() {
        return mRecord;
    }

    public void setRecord(RecordEntity record) {
        mRecord = record;
    }

    public String getOptionName() {
        return mOptionName;
    }

    public void setOptionName(String optionName) {
        mOptionName = optionName;
    }

    public ForeignCollection<VoteEntity> getVotes() {
        return mVotes;
    }

    public void setVotes(ForeignCollection<VoteEntity> votes) {
        mVotes = votes;
    }

    @Override
    public String toString() {
        return "OptionEntity{" +
                "mRecord=" + mRecord +
                ", mOptionName='" + mOptionName + '\'' +
                ", mVotes=" + mVotes.toString() +
                '}';
    }
}
