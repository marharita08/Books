package com.example.books.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFilters {
    private List<Genre> genres;
    private List<Author> authors;

    public SearchFilters() {
        genres = new ArrayList<>();
        authors = new ArrayList<>();
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchFilters that = (SearchFilters) o;
        return Objects.equals(genres, that.genres) && Objects.equals(authors, that.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genres, authors);
    }

    @Override
    public String toString() {
        return "SearchFilters{" +
                "genres=" + genres +
                ", authors=" + authors +
                '}';
    }

    @JsonIgnore
    public boolean isEmpty() {
        return genres.isEmpty() && authors.isEmpty();
    }
}

