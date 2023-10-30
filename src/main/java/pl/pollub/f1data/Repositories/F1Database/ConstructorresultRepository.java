package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Constructorresult;

/**
 * This interface is responsible for handling queries related to constructor results.
 */
public interface ConstructorresultRepository extends JpaRepository<Constructorresult, Integer> {
}