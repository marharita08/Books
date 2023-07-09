package sumdu.edu.ua.entities;

import java.util.List;
import java.util.Objects;

public class Book {
    private int id;
    private String title;
    private String description;
    private String coverPhoto;
    private int copyright;
    private int pageAmount;
    private List<Author> authors;
    private List<Genre> genres;

    public Book() {
    }

    public Book(int id, String title, String coverPhoto) {
        this.id = id;
        this.title = title;
        this.coverPhoto = coverPhoto;
    }

    public Book(int id, String title, String description, String coverPhoto, int copyright, int pageAmount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coverPhoto = coverPhoto;
        this.copyright = copyright;
        this.pageAmount = pageAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    public int getPageAmount() {
        return pageAmount;
    }

    public void setPageAmount(int pageAmount) {
        this.pageAmount = pageAmount;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && copyright == book.copyright && pageAmount == book.pageAmount && Objects.equals(title, book.title) && Objects.equals(description, book.description) && Objects.equals(coverPhoto, book.coverPhoto) && Objects.equals(authors, book.authors) && Objects.equals(genres, book.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, coverPhoto, copyright, pageAmount, authors, genres);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", cover_photo='" + coverPhoto + '\'' +
                ", first_publication_year=" + copyright +
                ", page_amount=" + pageAmount +
                ", authors=" + authors +
                ", genres=" + genres +
                '}';
    }
}
