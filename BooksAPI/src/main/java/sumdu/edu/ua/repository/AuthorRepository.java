package sumdu.edu.ua.repository;

import sumdu.edu.ua.entities.Author;

import java.util.List;

public interface AuthorRepository {

    int save(Author author);
    void update(Author author);
    void delete(int id);
    List<Author> findAll();
    List<Author> findByBookId(int bookId);
    boolean checkExistence(String fullName);
    Author findByName(String fullName);
    String getPhoto(int id);
    List<Author> getAuthorsForSearch();
}
