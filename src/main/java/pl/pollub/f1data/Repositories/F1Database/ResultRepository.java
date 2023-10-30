package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pollub.f1data.Models.Data.Result;

import java.util.Optional;
import java.util.List;

/**
 * Repository for {@link Result}
 */
public interface ResultRepository extends JpaRepository<Result, Integer> {

    /**
     * This method finds the fastest lap (result) in a given race.
     * @param raceId race id
     * @return {@link Result} if data was found, empty optional otherwise
     */
    @Query("SELECT r FROM Result r WHERE r.race.id = :raceId AND r.fastestLapTime IS NOT NULL ORDER BY r.fastestLapTime ASC LIMIT 1")
    Optional<Result> findFastestLapByRaceId(@Param("raceId") Integer raceId);

    /**
     * This method finds all results for a given race.
     * @param raceid race id
     * @return list of {@link Result}, can be empty
     */
    List<Result> findByRaceId(Integer raceid);

}