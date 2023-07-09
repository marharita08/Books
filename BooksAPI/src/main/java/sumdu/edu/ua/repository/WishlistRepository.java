package sumdu.edu.ua.repository;

import sumdu.edu.ua.entities.Book;

import java.util.List;

public interface WishlistRepository {
    List<Book> getWishlist(String username);
    void save(int bookId, String username);
    void delete(int bookId, String username);
    boolean isBookInWishList(int bookId, String username);
}
