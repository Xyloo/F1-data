package pl.pollub.f1data.Services;
import pl.pollub.f1data.Models.User;

import java.util.Optional;
import java.util.List;

public interface UserService {
    Optional<User> CreateUser(User user);
    String GenerateToken(User user);
    Optional<User> GetUserByUsername(String username);
    Optional<List<User>> GetUsers();
    boolean VerifyPassword(User user, String providedPassword);
}
