package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pollub.f1data.Models.Data.Pitstop;
import pl.pollub.f1data.Models.Data.PitstopId;
import java.util.List;
public interface PitstopRepository extends JpaRepository<Pitstop, PitstopId> {
    @Query("SELECT p.lap, COUNT(*) FROM Pitstop p WHERE p.race.id = :raceId GROUP BY p.lap")
    List<Object[]> findPitstopsByRaceGroupedByLap(@Param("raceId") Integer raceId);

}