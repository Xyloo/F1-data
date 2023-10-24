package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pollub.f1data.Models.Data.Race;
import java.util.List;

public interface RaceRepository extends JpaRepository<Race, Integer> {

    List<Race> getByCircuitId(Integer circuitId);
    @Query("select r from Race r where r.year.id = :year order by r.round")
    List<Race> findAllByYear(@Param("year") Integer year);
}