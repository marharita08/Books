package sumdu.edu.ua.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import sumdu.edu.ua.entities.Author;
import sumdu.edu.ua.entities.Book;
import sumdu.edu.ua.entities.Genre;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

@Repository
public class JdbcBookRepository implements BookRepository {
    private static final String INSERT_BOOK_SQL = "Insert into book values (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_BOOK_GENRE_SQL = "Insert into book_genre values (?, ?)";
    private static final String INSERT_BOOK_AUTHOR_SQL = "Insert into book_author values (?, ?)";
    private static final String COUNT_BOOKS_BY_ID = "select count(*) from book where book_id=?";
    private static final String SELECT_BOOKS_BY_GENRE_SQL = "select * from book b " +
            "join book_genre bg on b.book_id=bg.book_id and genre_id=? order by copyright desc, title";
    private static final String SELECT_BOOKS_BY_AUTHOR_SQL = "select * from book b " +
            "join book_author ba on b.book_id=ba.book_id and author_id=? order by copyright desc, title";
    private static final String SELECT_BOOK_BY_ID_SQL = "select * from book where book_id=?";
    private static final String SELECT_SIMILAR_BOOKS_SQL = "select *, count(*) amount from( " +
            "select b.book_id, title, cover_photo " +
            "from book b " +
            "join book_genre bg on b.book_id=bg.book_id " +
            "join book_genre bbg on bbg.genre_id=bg.genre_id and bbg.book_id = ?" +
            "union all " +
            "select b.book_id, title, cover_photo " +
            "from book b " +
            "join book_author ba on ba.book_id=b.book_id " +
            "join book_author bba on bba.author_id=ba.author_id and bba.book_id=?) tab " +
            "where book_id!=? " +
            "group by book_id, title, cover_photo " +
            "order by amount desc, random() " +
            "limit 10";
    private static final String SEARCH_BY_TITLE_SQL = "select * from book b where title ilike ?";
    private static final String GENRE_FILTER_SQL = " and ? in (select genre_id from book_genre bg where bg.book_id=b.book_id) ";
    private static final String AUTHOR_FILTER_SQL = " and ? in (select author_id from book_author ba where ba.book_id=b.book_id)";
    private final JdbcTemplate jdbcTemplate;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public JdbcBookRepository(JdbcTemplate jdbcTemplate, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public void save(Book book) {

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement =
                    con.prepareStatement(INSERT_BOOK_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, book.getId());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getDescription());
            preparedStatement.setString(4, book.getCoverPhoto());
            if (book.getCopyright() != 0) {
                preparedStatement.setInt(5, book.getCopyright());
            } else {
                preparedStatement.setNull(5, Types.INTEGER);
            }
            if (book.getPageAmount() != 0) {
                preparedStatement.setInt(6, book.getPageAmount());
            } else {
                preparedStatement.setNull(6, Types.INTEGER);
            }
            return preparedStatement;
        });
        for (Author author:book.getAuthors()) {
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con.prepareStatement(INSERT_BOOK_AUTHOR_SQL);
                preparedStatement.setInt(1, book.getId());
                preparedStatement.setInt(2, author.getId());
                return preparedStatement;
            });
        }
        for (Genre genre :book.getGenres()) {
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con.prepareStatement(INSERT_BOOK_GENRE_SQL);
                preparedStatement.setInt(1, book.getId());
                preparedStatement.setInt(2, genre.getId());
                return preparedStatement;
            });
        }
    }

    @Override
    public boolean checkExistence(int id) {
        Integer count = jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(COUNT_BOOKS_BY_ID);
            preparedStatement.setInt(1, id);
            return preparedStatement;
        }, (ResultSetExtractor<Integer>) rs -> rs.getInt("count"));
        return count != null && count != 0;
    }

    @Override
    public List<Book> findByGenreId(int genreId) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_BOOKS_BY_GENRE_SQL);
            preparedStatement.setInt(1, genreId);
            return preparedStatement;
        }, (rs, rowNum) ->
            new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("cover_photo")
            )
        );
    }

    @Override
    public List<Book> findByAuthorId(int authorId) {
        return jdbcTemplate.query(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(SELECT_BOOKS_BY_AUTHOR_SQL);
                    preparedStatement.setInt(1, authorId);
                    return preparedStatement;
                }, (rs, rowNum) ->
                        new Book(
                                rs.getInt("book_id"),
                                rs.getString("title"),
                                rs.getString("cover_photo")
                        )
        );
    }

    @Override
    public Book findById(int id) {
        Book book = jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_BOOK_BY_ID_SQL);
            preparedStatement.setInt(1, id);
            return preparedStatement;
        }, rs -> {
                rs.next();
                return new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("cover_photo"),
                        rs.getInt("copyright"),
                        rs.getInt("page_amount")
                );
            }
        );
        book.setAuthors(authorRepository.findByBookId(id));
        book.setGenres(genreRepository.findByBookId(id));
        return book;
    }

    @Override
    public List<Book> findRelatedBooks(int id) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_SIMILAR_BOOKS_SQL);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            preparedStatement.setInt(3, id);
            return preparedStatement;
        }, (rs, rowNum) ->
                        new Book(
                                rs.getInt("book_id"),
                                rs.getString("title"),
                                rs.getString("cover_photo")
                        )
                );
    }

    @Override
    public List<Book> searchByTitle(String title, List<Genre> genres, List<Author> authors) {
        return jdbcTemplate.query(con -> {
            String sql = SEARCH_BY_TITLE_SQL;
            if (genres != null) {
                for (int i = 0; i < genres.size(); i++) {
                    sql += GENRE_FILTER_SQL;
                }
            }
            if (authors != null) {
                for (int i = 0; i < authors.size(); i++) {
                    sql += AUTHOR_FILTER_SQL;
                }
            }
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%" + title + "%");
            int i = 2;
            if (genres != null) {
                for (Genre genre : genres) {
                    preparedStatement.setInt(i++, genre.getId());
                }
            }
            if (authors != null) {
                for (Author author : authors) {
                    preparedStatement.setInt(i++, author.getId());
                }
            }
            return preparedStatement;
        }, (rs, rowNum) ->
                new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("cover_photo")
                )
        );
    }
}
