package com.android.project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lobster on 21.05.16.
 */

public class Record{

    private String username;
    private String avatar;
    private Long recordId;
    private List<Image> images;
    private List<Option> options;

    public Record() {

    }

    public Record(String title, String avatar, Long recordId, List<Image> images, List<Option> options) {
        this.username = title;
        this.avatar = avatar;
        this.recordId = recordId;
        this.images = images;
        this.options = options;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getAllVotes(){
        List<String> votes = new ArrayList<>();
        for (Option o: options){
            if (o != null) {
                votes.addAll(o.getVotes());
            }
        }

        return votes;
    }

    @Override
    public String toString() {
        return "Record{" +
                "username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", recordId=" + recordId +
                ", images=" + images +
                ", options=" + options +
                '}';
    }
}
