package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Laptime;
import pl.pollub.f1data.Models.Data.LaptimeId;

import java.util.List;

/**
 * Repository for {@link Laptime}

 */
public interface LaptimeRepository extends JpaRepository<Laptime, LaptimeId> {

    /**
     * This method returns all lap times for given race id.
     * @param raceId race id
     * @return list of lap times, can be empty
     */
    List<Laptime> getLapTimesByRaceId(Integer raceId);
}