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

    public static Builder builder() {
        return new Builder();
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

    public Boolean isVoted() {
        return selectedOption != null;
    }

    public RecordEntity toEntity(RecordEntity entity) {
        entity.setRecordId(recordId);
        entity.setUsername(username);
        entity.setAvatarPath(avatar);
        entity.setDescription(description);
        entity.setSelectedOption(selectedOption);

        return entity;
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

    public static final class Builder {
        private Record record;

        private Builder() {
            record = new Record();
        }

        public Builder recordId(Long recordId) {
            record.setRecordId(recordId);
            return this;
        }

        public Builder username(String username) {
            record.setUsername(username);
            return this;
        }

        public Builder avatar(String avatar) {
            record.setAvatar(avatar);
            return this;
        }

        public Builder description(String description) {
            record.setDescription(description);
            return this;
        }

        public Builder selectedOption(String optionName) {
            record.setSelectedOption(optionName);
            return this;
        }

        public Builder images(List<Image> images) {
            record.setImages(images);
            return this;
        }

        public Builder options(List<Option> options) {
            record.setOptions(options);
            return this;
        }

        public Record build() {
            return record;
        }
    }
}
