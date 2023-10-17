package pl.pollub.f1data.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import pl.pollub.f1data.Models.User;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserRepository extends JpaRepository<User, Long> {

    @Async
    CompletableFuture<Optional<User>> getUserByUsername(String username);

    @Async
    CompletableFuture<Optional<User>> getUserByEmail(String email);

   /* Optional<User> getUser(int id);

    Optional<User> getUserByEmail(String email);

    List<User> getUsers();*/

}
