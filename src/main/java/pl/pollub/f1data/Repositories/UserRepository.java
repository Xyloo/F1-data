package pl.pollub.f1data.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User createUser(User user);

    void deleteUser(int id);

    Optional<User> getUser(int id);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    List<User> getUsers();

    User updateUser(User user);
}
