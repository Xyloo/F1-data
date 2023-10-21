package pl.pollub.f1data.Services;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.pollub.f1data.Models.User;

import java.util.Optional;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService extends UserDetailsService {
    CompletableFuture<Optional<UserDetails>> GetUserByUsername(String username);
    CompletableFuture<Optional<UserDetails>> GetUserById(Long id);
    CompletableFuture<List<User>> GetUsers();
    CompletableFuture<Optional<User>> GetUserByIdOrUsername(String queriedId, Long requestUserId);
    CompletableFuture<Optional<User>> GetUserByIdOrUsername(String queriedId);
    CompletableFuture<Optional<User>> UpdateUser(User user);
}
