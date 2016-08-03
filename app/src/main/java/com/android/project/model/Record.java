package com.android.project.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lobster on 21.05.16.
 */

public class Record{

    private String title;
    private String avatar;
    private Long recordId;
    private List<Image> images;
    private List<Option> options;
    
    public Record(String title, String avatar, Long recordId, List<Image> images, List<Option> options) {
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
            votes.addAll(o.getVotes());
        }

        return votes;
    }

    @Override
    public String toString() {
        return "Record{" +
                "title='" + title + '\'' +
                ", recordId=" + recordId +
                ", images=" + images +
                ", options=" + options +
                '}';
    }
}
