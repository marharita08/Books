package sumdu.edu.ua.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sumdu.edu.ua.entities.Author;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {
    private static final String INSERT_AUTHOR_SQL = "Insert into author (full_name, photo) values (?, ?)";
    private static final String UPDATE_AUTHOR_SQL = "Update author set full_name=?, photo=? where author_id=?";
    private static final String DELETE_AUTHOR_SQL = "Delete from author where author_id=?";
    private static final String SELECT_ALL_AUTHORS_SQL = "Select * from author order by full_name";
    private static final String SELECT_AUTHORS_BY_BOOK_SQL = "Select * from author a " +
            "join book_author ba on a.author_id=ba.author_id and ba.book_id=?";
    private static final String COUNT_AUTHORS_BY_NAME_SQL = "Select count(*) from author where full_name=?";
    private static final String SELECT_AUTHOR_BY_NAME_SQL = "Select * from author where full_name=?";
    private static final String SELECT_PHOTO_BY_ID_SQL = "Select photo from author where author_id=?";
    private static final String SELECT_AUTHORS_FOR_SEARCH_SQL = "Select author_id, full_name, count(book_id) from author " +
            "join book_author using(author_id) group by author_id, full_name order by count desc limit 10";
    private final JdbcTemplate jdbcTemplate;

    public JdbcAuthorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(INSERT_AUTHOR_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, author.getFullName());
            preparedStatement.setString(2, author.getPhoto());
            return preparedStatement;
        }, keyHolder);
        return (int) keyHolder.getKeys().get("author_id");
    }

    @Override
    public void update(Author author) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(UPDATE_AUTHOR_SQL);
            preparedStatement.setString(1, author.getFullName());
            preparedStatement.setString(2, author.getPhoto());
            preparedStatement.setInt(3, author.getId());
            return preparedStatement;
        });
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(DELETE_AUTHOR_SQL);
            preparedStatement.setInt(1, id);
            return preparedStatement;
        });
    }

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query(SELECT_ALL_AUTHORS_SQL,
                (rs, rowNum) ->
                        new Author(
                                rs.getInt("author_id"),
                                rs.getString("full_name"),
                                rs.getString("photo")
                        )
                );
    }

    @Override
    public List<Author> findByBookId(int bookId) {
        return jdbcTemplate.query(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(SELECT_AUTHORS_BY_BOOK_SQL);
                    preparedStatement.setInt(1, bookId);
                    return preparedStatement;
                },
                (rs, rowNum)->
                        new Author(
                                rs.getInt("author_id"),
                                rs.getString("full_name"),
                                rs.getString("photo")
                        )
                );
    }

    @Override
    public boolean checkExistence(String fullName) {
        Integer count = jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(COUNT_AUTHORS_BY_NAME_SQL);
            preparedStatement.setString(1, fullName);
            return preparedStatement;
        }, rs -> rs.next() ? rs.getInt("count") : 0);
        return count != null && count != 0;
    }

    @Override
    public Author findByName(String fullName) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_AUTHOR_BY_NAME_SQL);
            preparedStatement.setString(1, fullName);
            return preparedStatement;
        }, rs ->
            rs.next() ? new Author(
                    rs.getInt("author_id"),
                    rs.getString("full_name"),
                    rs.getString("photo")) : null);
    }

    @Override
    public String getPhoto(int id) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_PHOTO_BY_ID_SQL);
            preparedStatement.setInt(1, id);
            return preparedStatement;
        }, rs -> rs.next() ? rs.getString("photo") : null);
    }

    @Override
    public List<Author> getAuthorsForSearch() {
        return jdbcTemplate.query(SELECT_AUTHORS_FOR_SEARCH_SQL,
                (rs, rowNum) ->
                        new Author(
                                rs.getInt("author_id"),
                                rs.getString("full_name")
                        )
        );
    }
}
