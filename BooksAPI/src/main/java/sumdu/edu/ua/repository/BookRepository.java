package sumdu.edu.ua.repository;

import sumdu.edu.ua.entities.Author;
import sumdu.edu.ua.entities.Book;
import sumdu.edu.ua.entities.Genre;

import java.util.List;

public interface BookRepository {
    void save(Book book);
    boolean checkExistence(int id);
    List<Book> findByGenreId(int genreId);
    List<Book> findByAuthorId(int authorId);
    Book findById(int id);
    List<Book> findRelatedBooks(int id);
    List<Book> searchByTitle(String title, List<Genre> genres, List<Author> authors);
}
