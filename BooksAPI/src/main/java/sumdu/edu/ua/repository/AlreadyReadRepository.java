package sumdu.edu.ua.repository;

import sumdu.edu.ua.entities.Book;
import sumdu.edu.ua.entities.Review;

import java.util.List;

public interface AlreadyReadRepository {
    String getAverageRating(int bookId);
    String getMyRating(int bookId, String username);
    List<Review> getReviews(int bookId);
    List<Book> getAlreadyReadList(String username);
    boolean isBookInAlreadyRead(String username, int bookId);
    void delete(String username, int bookId);
    void insert(Review review);
    void update(Review review);
    Review getReview(int bookId, String username);
}
