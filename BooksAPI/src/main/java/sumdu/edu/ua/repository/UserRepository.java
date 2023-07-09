package sumdu.edu.ua.repository;

import sumdu.edu.ua.entities.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    void update(User user);
    void delete(String username);
    Optional<User> findByUsername(String username);
    String getAvatar(String username);
    void updateAvatar(String avatar, String username);
    boolean existsByUsername(String username);
}
