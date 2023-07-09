package sumdu.edu.ua.repository;

import sumdu.edu.ua.entities.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findAll();
    List<Genre> findByBookId(int bookId);
    List<Genre> findGenresForSearch();
}
