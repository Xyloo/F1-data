package pl.pollub.f1data.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import pl.pollub.f1data.Models.ERole;
import pl.pollub.f1data.Models.Role;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Repository for {@link Role}
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * This method finds a role by name.
     * @param name role name {@link ERole}
     * @return {@link Role} if data was found, empty optional otherwise
     */
    @Async
    CompletableFuture<Optional<Role>> getRoleByName(ERole name);

}
