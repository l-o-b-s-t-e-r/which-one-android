package com.android.project.model;

/**
 * Created by Lobster on 31.05.16.
 */

public class Image{

    private Long recordId;
    private String image;

    public Image(Long recordId, String image) {
        this.recordId = recordId;
        this.image = image;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
