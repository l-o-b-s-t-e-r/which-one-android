package com.android.project.model;

import java.util.List;

/**
 * Created by Lobster on 01.06.16.
 */
public class Option{

    private Long optionId;
    private String optionName;

    private List<String> votes;

    public Option(Long optionId, String optionName, List<String> votes) {
        this.optionId = optionId;
        this.optionName = optionName;
        this.votes = votes;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
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
                "optionId=" + optionId +
                ", optionName='" + optionName + '\'' +
                ", votes=" + votes +
                '}';
    }
}
