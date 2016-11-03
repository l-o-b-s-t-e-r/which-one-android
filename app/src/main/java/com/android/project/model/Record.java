package com.android.project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lobster on 21.05.16.
 */

public class Record{

    private Long recordId;
    private String username;
    private String avatar;
    private String description;
    private String selectedOption;
    private List<Image> images;
    private List<Option> options;

    public Record() {

    }

    public Record(String username, String avatar, Long recordId, String description, String selectedOption, List<Image> images, List<Option> options) {
        this.username = username;
        this.avatar = avatar;
        this.recordId = recordId;
        this.description = description;
        this.selectedOption = selectedOption;
        this.images = images;
        this.options = options;
    }

    public Record(RecordEntity entity) {
        setRecordId(entity.getRecordId());
        setUsername(entity.getUsername());
        setAvatar(entity.getAvatarPath());
        setDescription(entity.getDescription());
        setSelectedOption(entity.getSelectedOption());

        List<Image> images = new ArrayList<>();
        for (ImageEntity imageEntity : entity.getImages()) {
            images.add(new Image(imageEntity.getImagePath()));
        }
        setImages(images);

        List<Option> options = new ArrayList<>();
        for (OptionEntity optionEntity : entity.getOptions()) {
            options.add(new Option(optionEntity.getOptionName(), optionEntity.getVoteCount()));
        }
        setOptions(options);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public int getVoteCount() {
        int voteCount = 0;
        for (Option option : options) {
            voteCount += option.getVoteCount();
        }

        return voteCount;
    }

    public Option getOption(String optionName) {
        for (Option option : options) {
            if (option.getOptionName().equals(optionName)) {
                return option;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        return recordId != null ? recordId.equals(record.recordId) : record.recordId == null;

    }

    @Override
    public int hashCode() {
        return recordId != null ? recordId.hashCode() : 0;
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
