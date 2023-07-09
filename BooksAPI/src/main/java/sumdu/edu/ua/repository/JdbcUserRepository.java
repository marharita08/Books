package sumdu.edu.ua.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sumdu.edu.ua.entities.User;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final String INSERT_USER_SQL = "Insert into users values (?, ?, ?, ?)";
    private static final String UPDATE_USER_SQL = "Update users set full_name=?, password=? where username=?";
    private static final String DELETE_USER_SQL = "Delete from users where username=?";
    private static final String SELECT_USER_SQL = "Select * from users where username=?";
    private static final String GET_AVATAR_SQL = "Select avatar from users where username=?";
    private static final String UPDATE_AVATAR_SQL = "Update users set avatar=? where username=?";
    private static final String COUNT_BY_USERNAME = "Select count(*) from users where username=?";
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(INSERT_USER_SQL);
            preparedStatement.setString(1, user.getUsername());
            if (user.getFullName().isEmpty()) {
                preparedStatement.setNull(2, Types.VARCHAR);
            } else {
                preparedStatement.setString(2, user.getFullName());
            }
            preparedStatement.setString(3, user.getPassword());
            if (user.getAvatar().isEmpty()) {
                preparedStatement.setNull(4, Types.VARCHAR);
            } else {
                preparedStatement.setString(4, user.getAvatar());
            }
            return preparedStatement;
        });
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(UPDATE_USER_SQL);
            if (user.getFullName().isEmpty()) {
                preparedStatement.setNull(1, Types.VARCHAR);
            } else {
                preparedStatement.setString(1, user.getFullName());
            }
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUsername());
            return preparedStatement;
        });
    }

    @Override
    public void delete(String username) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(DELETE_USER_SQL);
            preparedStatement.setString(1, username);
            return preparedStatement;
        });
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_USER_SQL);
            preparedStatement.setString(1, username);
            return preparedStatement;
        },  rs -> {
                rs.next();
                return Optional.of(new User(
                        username,
                        rs.getString("full_name"),
                        rs.getString("password"),
                        rs.getString("avatar")
                ));
        });

    }

    @Override
    public String getAvatar(String username) {
        return jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(GET_AVATAR_SQL);
            preparedStatement.setString(1, username);
            return preparedStatement;
        }, rs -> rs.next() ? rs.getString("avatar") : null);
    }

    @Override
    public void updateAvatar(String avatar, String username) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(UPDATE_AVATAR_SQL);
            preparedStatement.setString(1, avatar);
            preparedStatement.setString(2, username);
            return preparedStatement;
        });
    }

    @Override
    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.query(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(COUNT_BY_USERNAME);
            preparedStatement.setString(1, username);
            return preparedStatement;
        }, rs -> rs.next() ? rs.getInt("count") : 0);
        return count != null && count != 0;
    }
}
