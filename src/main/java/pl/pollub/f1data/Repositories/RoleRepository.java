package pl.pollub.f1data.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import pl.pollub.f1data.Models.ERole;
import pl.pollub.f1data.Models.Role;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Async
    CompletableFuture<Optional<Role>> getRoleByName(ERole name);

}
