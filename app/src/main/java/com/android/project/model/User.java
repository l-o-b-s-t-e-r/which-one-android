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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (avatar != null ? !avatar.equals(user.avatar) : user.avatar != null) return false;
        return background != null ? background.equals(user.background) : user.background == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (background != null ? background.hashCode() : 0);
        return result;
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
