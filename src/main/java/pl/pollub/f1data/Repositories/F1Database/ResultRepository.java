package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pollub.f1data.Models.Data.Result;

import java.util.Optional;
import java.util.List;
public interface ResultRepository extends JpaRepository<Result, Integer> {

    @Query("SELECT r FROM Result r WHERE r.race.id = :raceId AND r.fastestLapTime IS NOT NULL ORDER BY r.fastestLapTime ASC LIMIT 1")
    Optional<Result> findFastestLapByRaceId(@Param("raceId") Integer raceId);
    @Query("SELECT r FROM Result r WHERE r.race.year.id = :year AND r.fastestLapTime IS NOT NULL ORDER BY r.fastestLapTime ASC LIMIT 1")
    Optional<Result> findFastestLapByYear(@Param("year") Integer year);
    List<Result> findByRaceId(Integer raceid);

}