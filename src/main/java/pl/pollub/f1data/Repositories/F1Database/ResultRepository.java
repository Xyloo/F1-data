package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pollub.f1data.Models.Data.Result;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Integer> {

    @Query("SELECT r FROM Result r WHERE r.race.id = :raceId AND r.fastestLapTime IS NOT NULL ORDER BY r.fastestLapTime ASC LIMIT 1")
    Optional<Result> findFastestLapByRaceId(@Param("raceId") Integer raceId);



}