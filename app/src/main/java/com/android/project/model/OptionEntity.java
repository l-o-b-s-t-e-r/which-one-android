package com.android.project.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Lobster on 14.09.16.
 */

public class OptionEntity {

    public static final String OPTION_ID_FIELD_NAME = "id";
    public static final String RECORD_ID_FIELD_NAME = "record_id";
    public static final String OPTION_NAME_FIELD_NAME = "option_name";
    public static final String VOTE_COUNT_FIELD_NAME = "vote_count";

    @DatabaseField(generatedId = true, columnName = OPTION_ID_FIELD_NAME)
    private Integer id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = RECORD_ID_FIELD_NAME, index = true)
    private RecordEntity mRecord;
    @DatabaseField(columnName = OPTION_NAME_FIELD_NAME)
    private String mOptionName;
    @DatabaseField(columnName = VOTE_COUNT_FIELD_NAME)
    private Integer mVoteCount;


    public OptionEntity() {

    }

    public OptionEntity(RecordEntity record, String optionName, Integer voteCount) {
        mRecord = record;
        mOptionName = optionName;
        mVoteCount = voteCount;
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

    public Integer getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(Integer voteCount) {
        mVoteCount = voteCount;
    }

    @Override
    public String toString() {
        return "OptionEntity{" +
                "id=" + id +
                ", mOptionName='" + mOptionName + '\'' +
                ", mVoteCount=" + mVoteCount +
                '}';
    }
}
