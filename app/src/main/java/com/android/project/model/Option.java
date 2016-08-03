package com.android.project.model;

import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Lobster on 01.06.16.
 */
public class Option{

    private Long recordId;
    private String optionName;
    private List<String> votes;

    public Option(Long recordId, String optionName, List<String> votes) {
        this.recordId = recordId;
        this.optionName = optionName;
        this.votes = votes;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public List<String> getVotes() {
        return votes;
    }

    public void setVotes(List<String> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Option{" +
                "recordId=" + recordId +
                ", optionName='" + optionName + '\'' +
                ", votes=" + votes +
                '}';
    }
}
