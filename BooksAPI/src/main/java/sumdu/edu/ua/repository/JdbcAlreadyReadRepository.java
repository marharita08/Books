package sumdu.edu.ua.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sumdu.edu.ua.entities.Book;
import sumdu.edu.ua.entities.Review;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@Repository
public class JdbcAlreadyReadRepository implements AlreadyReadRepository {
    private static final String SELECT_AVERAGE_RATING_SQL =
            "select coalesce(round(avg(rating), 1)::varchar(3), '-') rating " +
                    "from already_read where book_id=?";
    private static final String SELECT_MY_RATING_SQL = "select coalesce(rating::varchar(3), '-') rating " +
            "from already_read where book_id=? and username=?";
    private static final String SELECT_REVIEWS_SQL = "Select * from already_read where book_id=? " +
            "and review is not null order by adding_date desc";
    private static final String SELECT_ALREADY_READ_LIST_SQL = "Select * from book join already_read using(book_id) " +
            "where username=? order by adding_date desc";
    private static final String COUNT_SQL = "Select count(*) from already_read where username=? and book_id=?";
    private static final String INSERT_SQL = "Insert into already_read values (?, ?, ?, ?, now())";
    private static final String UPDATE_SQL = "Update already_read set rating=?, review=? where book_id=? and username=?";
    private static final String DELETE_SQL = "Delete from already_read where book_id=? and username=?";
    private static final String SELECT_REVIEW_SQL = "Select * from already_read where book_id=? and username=?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcAlreadyReadRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getAverageRating(int bookId) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_AVERAGE_RATING_SQL);
            preparedStatement.setInt(1, bookId);
            return preparedStatement;
        }, rs -> rs.next()?rs.getString("rating"):"-");
    }

    @Override
    public String getMyRating(int bookId, String username) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_MY_RATING_SQL);
            preparedStatement.setInt(1, bookId);
            preparedStatement.setString(2, username);
            return preparedStatement;
        }, rs -> rs.next()?rs.getString("rating"):"-");
    }

    @Override
    public List<Review> getReviews(int bookId) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_REVIEWS_SQL);
            preparedStatement.setInt(1, bookId);
            return preparedStatement;
        }, (rs, rowNum) ->
            new Review(
                    rs.getString("username"),
                    rs.getInt("rating"),
                    rs.getString("review")
            )
        );
    }

    @Override
    public List<Book> getAlreadyReadList(String username) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_ALREADY_READ_LIST_SQL);
            preparedStatement.setString(1, username);
            return preparedStatement;
        }, (rs, rowNum) -> new Book(
                rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("cover_photo")
        ));
    }

    @Override
    public boolean isBookInAlreadyRead(String username, int bookId) {
        Integer count = jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(COUNT_SQL);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, bookId);
            return preparedStatement;
        }, rs -> rs.next() ? rs.getInt("count"):0);
        return count != null && count != 0;
    }

    @Override
    public void delete(String username, int bookId) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(DELETE_SQL);
            preparedStatement.setInt(1, bookId);
            preparedStatement.setString(2, username);
            return preparedStatement;
        });
    }

    @Override
    public void insert(Review review) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(INSERT_SQL);
            preparedStatement.setInt(1, review.getBookId());
            preparedStatement.setString(2, review.getUsername());
            if (review.getRating() == 0f) {
                preparedStatement.setNull(3, Types.INTEGER);
            } else {
                preparedStatement.setFloat(3, review.getRating());
            }
            if (review.getReview() == null || review.getReview().equals("")) {
                preparedStatement.setNull(4, Types.VARCHAR);
            } else {
                preparedStatement.setString(4, review.getReview());
            }
            return preparedStatement;
        });
    }

    @Override
    public void update(Review review) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(UPDATE_SQL);
            if (review.getRating() == 0) {
                preparedStatement.setNull(1, Types.INTEGER);
            } else {
                preparedStatement.setFloat(1, review.getRating());
            }
            if (review.getReview() == null || review.getReview().equals("")) {
                preparedStatement.setNull(2, Types.VARCHAR);
            } else {
                preparedStatement.setString(2, review.getReview());
            }
            preparedStatement.setInt(3, review.getBookId());
            preparedStatement.setString(4, review.getUsername());
            return preparedStatement;
        });
    }

    @Override
    public Review getReview(int bookId, String username) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_REVIEW_SQL);
            preparedStatement.setInt(1, bookId);
            preparedStatement.setString(2, username);
            return preparedStatement;
        }, rs -> rs.next() ? new Review(
                rs.getString("username"),
                rs.getInt("book_id"),
                rs.getFloat("rating"),
                rs.getString("review")
        ):null);
    }

}
