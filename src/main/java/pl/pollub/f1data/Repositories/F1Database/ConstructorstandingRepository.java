package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Constructorstanding;

/**
 * This interface is responsible for handling queries related to constructor standings.
 */
public interface ConstructorstandingRepository extends JpaRepository<Constructorstanding, Integer> {
}