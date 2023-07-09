package sumdu.edu.ua.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sumdu.edu.ua.entities.Book;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcReadingRepository implements ReadingRepository {

    private static final String SELECT_READING_LIST_SQL = "Select * from reading join book using(book_id) " +
            "where username=? order by adding_date desc";
    private static final String INSERT_BOOK_TO_READING_LIST_SQL = "Insert into reading values (?, ?, now())";
    private static final String DELETE_FROM_READING_LIST_SQL = "Delete from reading where username=? and book_id=?";
    private static final String COUNT_SQL = "Select count(*) from reading where username=? and book_id=?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcReadingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> getReadingList(String username) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_READING_LIST_SQL);
            preparedStatement.setString(1, username);
            return preparedStatement;
        }, (rs, rowNum) -> new Book(
                rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("cover_photo")
        ));
    }

    @Override
    public void save(int bookId, String username) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(INSERT_BOOK_TO_READING_LIST_SQL);
            preparedStatement.setInt(1, bookId);
            preparedStatement.setString(2, username);
            return preparedStatement;
        });
    }

    @Override
    public void delete(int bookId, String username) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(DELETE_FROM_READING_LIST_SQL);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, bookId);
            return preparedStatement;
        });
    }

    @Override
    public boolean isBookInReadingList(int bookId, String username) {
        Integer count = jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(COUNT_SQL);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, bookId);
            return preparedStatement;
        }, rs -> rs.next() ? rs.getInt("count"):0);
        return count != null && count != 0;
    }
}
