package sumdu.edu.ua.entities;

import java.util.Objects;

public class Review {
    private String username;
    private int bookId;
    private float rating;
    private String review;

    public Review() {
    }

    public Review(String username, int rating, String review) {
        this.username = username;
        this.rating = rating;
        this.review = review;
    }

    public Review(String username, int bookId, float rating, String review) {
        this.username = username;
        this.bookId = bookId;
        this.rating = rating;
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review1 = (Review) o;
        return bookId == review1.bookId && rating == review1.rating && Objects.equals(username, review1.username) && Objects.equals(review, review1.review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, bookId, rating, review);
    }

    @Override
    public String toString() {
        return "Review{" +
                "username='" + username + '\'' +
                ", bookId=" + bookId +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                '}';
    }
}
