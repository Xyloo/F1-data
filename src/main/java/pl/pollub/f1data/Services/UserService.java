package pl.pollub.f1data.Services;
import pl.pollub.f1data.Models.User;

import java.util.Optional;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<Optional<User>> createUser(User userData);
    String GenerateToken(User user);
    Optional<User> GetUserByUsername(String username);
    CompletableFuture<List<User>> GetUsers();
    boolean VerifyPassword(User user, String providedPassword);
}
