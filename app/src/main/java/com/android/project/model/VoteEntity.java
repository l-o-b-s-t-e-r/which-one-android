package com.android.project.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Lobster on 18.09.16.
 */
public class VoteEntity {

    public static final String RECORD_ID_FIELD_NAME = "record_id";
    public static final String OPTION_NAME_FIELD_NAME = "option_id";
    public static final String VOTED_USER_FIELD_NAME = "voted_user";

    @DatabaseField(generatedId = true, columnName = "id")
    private Integer id;
    @DatabaseField(columnName = RECORD_ID_FIELD_NAME)
    private Long mRecordId;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = OPTION_NAME_FIELD_NAME)
    private OptionEntity mOption;
    @DatabaseField(columnName = VOTED_USER_FIELD_NAME)
    private String mVotedUser;

    public VoteEntity() {

    }

    public VoteEntity(Long recordId, OptionEntity option, String votedUser) {
        mRecordId = recordId;
        mOption = option;
        mVotedUser = votedUser;
    }


    public Long getRecordId() {
        return mRecordId;
    }

    public void setRecordId(Long recordId) {
        mRecordId = recordId;
    }

    public OptionEntity getOption() {
        return mOption;
    }

    public void setOption(OptionEntity option) {
        mOption = option;
    }

    public String getVotedUser() {
        return mVotedUser;
    }

    public void setVotedUser(String votedUser) {
        mVotedUser = votedUser;
    }

    @Override
    public String toString() {
        return "VoteEntity{" +
                "mVotedUser='" + mVotedUser + '\'' +
                ", mOption=" + mOption.getOptionName() +
                ", mRecordId=" + mRecordId +
                '}';
    }
}
