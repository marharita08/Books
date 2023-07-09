package sumdu.edu.ua.repository;

import sumdu.edu.ua.entities.Book;

import java.util.List;

public interface ReadingRepository {
    List<Book> getReadingList(String username);
    void save(int bookId, String username);
    void delete(int bookId, String username);
    boolean isBookInReadingList(int bookId, String username);
}
