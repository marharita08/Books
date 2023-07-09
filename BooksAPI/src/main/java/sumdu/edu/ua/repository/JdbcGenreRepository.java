package sumdu.edu.ua.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sumdu.edu.ua.entities.Genre;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcGenreRepository implements GenreRepository {
    private static final String SELECT_ALL_GENRES_SQL = "Select distinct g.* from genre g " +
            "join book_genre bg on bg.genre_id=g.genre_id order by genre";
    private static final String SELECT_GENRES_BY_BOOK_SQL = "Select * from genre g " +
            "join book_genre bg on g.genre_id=bg.genre_id and book_id=?";
    private static final String SELECT_GENRES_FOR_SEARCH_SQL = "Select genre_id, genre, count(book_id) from genre " +
            "join book_genre using(genre_id) group by genre_id, genre order by count desc limit 10";
    private final JdbcTemplate jdbcTemplate;

    public JdbcGenreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(SELECT_ALL_GENRES_SQL,
                (rs, rowNum) ->
                        new Genre(
                                rs.getInt("genre_id"),
                                rs.getString("genre")
                        )
                );
    }

    @Override
    public List<Genre> findByBookId(int bookId) {
        return jdbcTemplate.query(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(SELECT_GENRES_BY_BOOK_SQL);
                    preparedStatement.setInt(1, bookId);
                    return preparedStatement;
                },
                (rs, rowNum) ->
                        new Genre(
                                rs.getInt("genre_id"),
                                rs.getString("genre")
                        )
        );
    }

    @Override
    public List<Genre> findGenresForSearch() {
        return jdbcTemplate.query(SELECT_GENRES_FOR_SEARCH_SQL,
                (rs, rowNum) ->
                        new Genre(
                                rs.getInt("genre_id"),
                                rs.getString("genre")
                        )
        );
    }
}
