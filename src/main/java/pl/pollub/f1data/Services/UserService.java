package pl.pollub.f1data.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.pollub.f1data.Models.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Service for {@link User}
 */
public interface UserService extends UserDetailsService {
    /**
     * Finds user by username
     * @param username username
     * @return {@link Optional} of {@link UserDetails}
     */
    CompletableFuture<Optional<UserDetails>> getUserByUsername(String username);

    /**
     * Finds user by id
     * @param id id
     * @return {@link Optional} of {@link UserDetails}
     */
    CompletableFuture<Optional<UserDetails>> getUserById(Long id);

    /**
     * Gets all users
     * @return {@link List} of {@link User}
     */
    CompletableFuture<List<User>> getUsers();

    /**
     * Finds a user by either id or username.
     * If the user is not found, returns empty {@link Optional}
     * Request user id is required to check if requesting user is allowed to see queried user's sensitive data
     * @param queriedId id or username
     * @param requestUserId id of requesting user
     * @return {@link Optional} of {@link User}
     */
    CompletableFuture<Optional<User>> getUserByIdOrUsername(String queriedId, Long requestUserId);

    /**
     * Finds a user by either id or username.
     * If the user is not found, returns empty {@link Optional}
     * This overload is used when requesting user is already known to be trusted (e.g. when requesting user is the same as queried user or when requesting user is an admin)
     * @param queriedId id or username
     * @return {@link Optional} of {@link User}
     */
    CompletableFuture<Optional<User>> getUserByIdOrUsername(String queriedId);

    /**
     * Updates user with new data
     * @param user user
     * @return {@link Optional} of {@link User}
     */
    CompletableFuture<Optional<User>> updateUser(User user);

    /**
     * Deletes user by id
     * @param id id
     * @return ResponseEntity with status code 200 if successful, 404 if user not found
     */
    CompletableFuture<ResponseEntity<?>> deleteUser(Long id);
}
