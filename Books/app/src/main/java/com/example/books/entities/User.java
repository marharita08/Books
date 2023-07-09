package com.example.books.entities;

import java.util.Objects;

public class User {
    private String username;
    private String fullName;
    private String password;
    private String avatar;

    public User(String username, String fullName, String password, String avatar) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.avatar = avatar;
    }

    public User() {
    }

    public User(String username, String fullName, String password) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username)
                && Objects.equals(fullName, user.fullName)
                && Objects.equals(password, user.password)
                && Objects.equals(avatar, user.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, fullName, password, avatar);
    }
}
