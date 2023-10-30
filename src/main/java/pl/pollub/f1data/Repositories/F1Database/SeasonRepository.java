package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Season;

/**
 * Repository for {@link Season}
 */
public interface SeasonRepository extends JpaRepository<Season, Integer> {
}