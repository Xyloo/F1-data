package pl.pollub.f1data.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import pl.pollub.f1data.Models.User;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Repository for {@link User}
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds user by username
     * @param username username
     * @return {@link Optional} of {@link User}
     */
    @Async
    CompletableFuture<Optional<User>> getUserByUsername(String username);

    /**
     * Finds user by email
     * @param email email
     * @return {@link Optional} of {@link User}
     */
    @Async
    CompletableFuture<Optional<User>> getUserByEmail(String email);

    /**
     * Finds user by id
     * @param id id
     * @return {@link Optional} of {@link User}
     */
    @Async
    CompletableFuture<Optional<User>> getUserById(Long id);

}
