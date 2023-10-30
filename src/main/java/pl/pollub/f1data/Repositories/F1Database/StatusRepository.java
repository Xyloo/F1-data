package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Status;

/**
 * Repository for {@link Status}
 */
public interface StatusRepository extends JpaRepository<Status, Integer> {
}