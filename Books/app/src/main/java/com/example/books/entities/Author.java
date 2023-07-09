package com.example.books.entities;

import java.util.Objects;

public class Author {
    private int id;
    private String fullName;
    private String photo;

    public Author(String fullName) {
        this.fullName = fullName;
    }

    public Author(int id, String fullName, String photo) {
        this.id = id;
        this.fullName = fullName;
        this.photo = photo;
    }

    public Author() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id && Objects.equals(fullName, author.fullName) && Objects.equals(photo, author.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, photo);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
