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
        return User.builder()
                .username("username")
                .password("password")
                .avatar("avatar.jpg")
                .background("background.jpg").build();
    }

    public String getUserJson() {
        return "{" +
                "\"username\":\"username\"," +
                "\"password\":\"password\"," +
                "\"avatar\":\"avatar.jpg\"," +
                "\"background\":\"background.jpg\"," +
                "\"email\":\"email\"," +
                "\"verified\":true," +
                "\"verificationCode\":null" +
                "}";
    }

    public List<User> getUsers() {
        return Arrays.asList(
                User.builder()
                        .username("username1")
                        .password("password1")
                        .avatar("avatar1.jpg")
                        .background("background1.jpg").build(),
                User.builder()
                        .username("username2")
                        .password("password2")
                        .avatar("avatar2.jpg")
                        .background("background2.jpg").build(),
                User.builder()
                        .username("username3")
                        .password("password3")
                        .avatar("avatar3.jpg")
                        .background("background3.jpg").build(),
                User.builder()
                        .username("username4")
                        .password("password4")
                        .avatar("avatar4.jpg")
                        .background("background4.jpg").build()
        );
    }

    public String getUsersJson() {
        return "[" +
                "{" +
                "\"username\":\"username1\"," +
                "\"password\":\"password1\"," +
                "\"avatar\":\"avatar1.jpg\"," +
                "\"background\":\"background1.jpg\"," +
                "\"email\":\"email1\"," +
                "\"verified\":true," +
                "\"verificationCode\":null" +
                "}," +
                "{" +
                "\"username\":\"username2\"," +
                "\"password\":\"password2\"," +
                "\"avatar\":\"avatar2.jpg\"," +
                "\"background\":\"background2.jpg\"," +
                "\"email\":\"email2\"," +
                "\"verified\":true," +
                "\"verificationCode\":null" +
                "}," +
                "{" +
                "\"username\":\"username3\"," +
                "\"password\":\"password3\"," +
                "\"avatar\":\"avatar3.jpg\"," +
                "\"background\":\"background3.jpg\"," +
                "\"email\":\"email3\"," +
                "\"verified\":true," +
                "\"verificationCode\":null" +
                "}," +
                "{" +
                "\"username\":\"username4\"," +
                "\"password\":\"password4\"," +
                "\"avatar\":\"avatar4.jpg\"," +
                "\"background\":\"background4.jpg\"," +
                "\"email\":\"email4\"," +
                "\"verified\":true," +
                "\"verificationCode\":null" +
                "}" +
                "]";
    }

    public Record getRecord() {
        List<Image> images = Arrays.asList(
                new Image("image-name-1.jpg"),
                new Image("image-name-2.jpg"),
                new Image("image-name-3.jpg")
        );

        String selectedOption = "selected-option-4";
        List<Option> options = Arrays.asList(
                new Option("option-name-1", 10),
                new Option("option-name-2", 5),
                new Option("option-name-3", 0),
                new Option(selectedOption, 25)

        );

        return Record.builder()
                .recordId(1L)
                .username("username1")
                .avatar("avatar1.jpg")
                .description("description1")
                .selectedOption(selectedOption)
                .images(images)
                .options(options).build();
    }

    public String getVotedRecordJson() {
        return "{" +
                "\"recordId\":1," +
                "\"username\":\"username1\"," +
                "\"avatar\":\"avatar1.jpg\"," +
                "\"description\":\"description1\"," +
                "\"selectedOption\":\"selected-option-4\"," +
                "\"images\":[{\"image\":\"image-name-1.jpg\"},{\"image\":\"image-name-2.jpg\"},{\"image\":\"image-name-3.jpg\"}]," +
                "\"options\":[{\"optionName\":\"option-name-1\",\"voteCount\":10},{\"optionName\":\"option-name-2\",\"voteCount\":5},{\"optionName\":\"option-name-3\",\"voteCount\":0},{\"optionName\":\"selected-option-4\",\"voteCount\":25}]" +
                "}";
    }

    public String getNoVotedRecordJson() {
        return "{" +
                "\"recordId\":1," +
                "\"username\":\"username1\"," +
                "\"avatar\":\"avatar1.jpg\"," +
                "\"description\":\"description1\"," +
                "\"selectedOption\":\"null\"," +
                "\"images\":[{\"image\":\"image-name-1.jpg\"},{\"image\":\"image-name-2.jpg\"},{\"image\":\"image-name-3.jpg\"}]," +
                "\"options\":[{\"optionName\":\"option-name-1\",\"voteCount\":10},{\"optionName\":\"option-name-2\",\"voteCount\":5},{\"optionName\":\"option-name-3\",\"voteCount\":0},{\"optionName\":\"selected-option-4\",\"voteCount\":24}]" +
                "}";
    }

    public List<Record> getRecords() {
        List<Record> records = new ArrayList<>();
        Record record;
        String selectedOption;
        List<Image> images;
        List<Option> options;

        images = Arrays.asList(
                new Image("image-name-1.1.jpg"),
                new Image("image-name-1.2.jpg"),
                new Image("image-name-1.3.jpg")
        );

        selectedOption = "option-name-4";
        options = Arrays.asList(
                new Option("option-name-1", 10),
                new Option("option-name-2", 5),
                new Option("option-name-3", 0),
                new Option(selectedOption, 25)

        );

        record = Record.builder()
                .recordId(1L)
                .username("username1")
                .avatar("avatar1.jpg")
                .description("description1")
                .selectedOption(selectedOption)
                .images(images)
                .options(options).build();

        records.add(record);

        images = Arrays.asList(
                new Image("image-name-2.1.jpg"),
                new Image("image-name-2.2.jpg"),
                new Image("image-name-2.3.jpg"),
                new Image("image-name-2.4.jpg")
        );

        options = Arrays.asList(
                new Option("option-name-1", 0),
                new Option("option-name-2", 5),
                new Option("option-name-3", 10)

        );

        record = Record.builder()
                .recordId(2L)
                .username("username2")
                .avatar("avatar2.jpg")
                .description("description2")
                .selectedOption(null)
                .images(images)
                .options(options).build();

        records.add(record);

        images = Arrays.asList(
                new Image("image-name-3.1.jpg"),
                new Image("image-name-3.2.jpg")
        );

        selectedOption = "option-name-1";
        options = Arrays.asList(
                new Option(selectedOption, 10),
                new Option("option-name-2", 5)

        );

        record = Record.builder()
                .recordId(3L)
                .username("username3")
                .avatar("avatar3.jpg")
                .description("description3")
                .selectedOption(selectedOption)
                .images(images)
                .options(options).build();

        records.add(record);

        return records;
    }

    public String getRecordsJson() {
        return "[" +
                "{" +
                "\"recordId\":1," +
                "\"username\":\"username1\"," +
                "\"avatar\":\"avatar1.jpg\"," +
                "\"description\":\"description1\"," +
                "\"selectedOption\":\"option-name-4\"," +
                "\"images\":[{\"image\":\"image-name-1.1.jpg\"},{\"image\":\"image-name-1.2.jpg\"},{\"image\":\"image-name-1.3.jpg\"}]," +
                "\"options\":[{\"optionName\":\"option-name-1\",\"voteCount\":10},{\"optionName\":\"option-name-2\",\"voteCount\":5},{\"optionName\":\"option-name-3\",\"voteCount\":0},{\"optionName\":\"option-name-4\",\"voteCount\":25}]" +
                "}," +
                "{" +
                "\"recordId\":2," +
                "\"username\":\"username2\"," +
                "\"avatar\":\"avatar2.jpg\"," +
                "\"description\":\"description2\"," +
                "\"selectedOption\":\"null\"," +
                "\"images\":[{\"image\":\"image-name-2.1.jpg\"},{\"image\":\"image-name-2.2.jpg\"},{\"image\":\"image-name-2.3.jpg\"},{\"image\":\"image-name-2.4.jpg\"}]," +
                "\"options\":[{\"optionName\":\"option-name-1\",\"voteCount\":0},{\"optionName\":\"option-name-2\",\"voteCount\":5},{\"optionName\":\"option-name-3\",\"voteCount\":10}]" +
                "}," +
                "{" +
                "\"recordId\":3," +
                "\"username\":\"username3\"," +
                "\"avatar\":\"avatar3.jpg\"," +
                "\"description\":\"description3\"," +
                "\"selectedOption\":\"option-name-1\"," +
                "\"images\":[{\"image\":\"image-name-3.1.jpg\"},{\"image\":\"image-name-3.2.jpg\"},{\"image\":\"image-name-3.3.jpg\"}]," +
                "\"options\":[{\"optionName\":\"option-name-1\",\"voteCount\":10},{\"optionName\":\"option-name-2\",\"voteCount\":5}]" +
                "}" +
                "]";
    }
}
