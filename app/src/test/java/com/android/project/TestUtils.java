package com.android.project;

import com.android.project.model.Image;
import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lobster on 10.11.16.
 */
public class TestUtils {

    public User getUser() {
        User user = new User();

        user.setUsername("username");
        user.setPassword("password");
        user.setAvatar("avatar-path");
        user.setBackground("background-path");

        return user;
    }

    public List<Record> getRecords() {
        List<Record> records = new ArrayList<>();
        Record record;
        String selectedOption;
        List<Image> images;
        List<Option> options;


        images = Arrays.asList(
                new Image("image-name-1.1"),
                new Image("image-name-1.2"),
                new Image("image-name-1.3")
        );

        selectedOption = "selected-option-4";
        options = Arrays.asList(
                new Option("option-name-1", 10),
                new Option("option-name-2", 5),
                new Option("option-name-3", 0),
                new Option(selectedOption, 25)

        );

        record = new Record("username1", "avatar1", 1L, "description1", selectedOption, images, options);
        records.add(record);

        images = Arrays.asList(
                new Image("image-name-2.1"),
                new Image("image-name-2.2"),
                new Image("image-name-2.3"),
                new Image("image-name-2.4")
        );

        options = Arrays.asList(
                new Option("option-name-1", 0),
                new Option("option-name-2", 5),
                new Option("option-name-3", 10)

        );

        record = new Record("username2", "avatar2", 2L, "description2", null, images, options);
        records.add(record);

        images = Arrays.asList(
                new Image("image-name-3.1"),
                new Image("image-name-3.2")
        );

        selectedOption = "option-name-1";
        options = Arrays.asList(
                new Option(selectedOption, 10),
                new Option("option-name-2", 5)

        );

        record = new Record("username3", "avatar3", 3L, "description3", selectedOption, images, options);
        records.add(record);

        return records;
    }
}
