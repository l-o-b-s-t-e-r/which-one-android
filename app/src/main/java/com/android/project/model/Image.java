package com.android.project.model;

/**
 * Created by Lobster on 31.05.16.
 */

public class Image{

    private String image;

    public Image(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Image{" +
                "image='" + image + '\'' +
                '}';
    }
}
