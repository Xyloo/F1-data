package pl.pollub.f1data.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.pollub.f1data.Models.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService extends UserDetailsService {
    CompletableFuture<Optional<UserDetails>> getUserByUsername(String username);
    CompletableFuture<Optional<UserDetails>> getUserById(Long id);
    CompletableFuture<List<User>> getUsers();
    CompletableFuture<Optional<User>> getUserByIdOrUsername(String queriedId, Long requestUserId);
    CompletableFuture<Optional<User>> getUserByIdOrUsername(String queriedId);
    CompletableFuture<Optional<User>> updateUser(User user);
    CompletableFuture<ResponseEntity<?>> deleteUser(Long id);
}
