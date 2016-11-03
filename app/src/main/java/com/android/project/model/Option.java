package com.android.project.model;

/**
 * Created by Lobster on 01.06.16.
 */
public class Option{

    private String optionName;
    private Integer voteCount;

    public Option(String optionName, Integer voteCount) {
        this.optionName = optionName;
        this.voteCount = voteCount;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "Option{" +
                ", optionName='" + optionName + '\'' +
                ", voteCount=" + voteCount +
                '}';
    }
}
