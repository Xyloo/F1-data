package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Circuit;

/**
 * Circuit data JPA repository
 */
public interface CircuitRepository extends JpaRepository<Circuit, Integer> {
}