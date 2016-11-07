package com.android.project.model;

/**
 * Created by Lobster on 26.07.16.
 */

public class User {

    private String username;
    private String password;
    private String avatar;
    private String background;

    public User() {

    }

    public User(UserEntity entity) {
        username = entity.getUsername();
        password = entity.getPassword();
        avatar = entity.getAvatar();
        background = entity.getBackground();
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public UserEntity toEntity(UserEntity entity) {
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setAvatar(avatar);
        entity.setBackground(background);

        return entity;
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", background='" + background + '\'' +
                '}';
    }
}
