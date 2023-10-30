package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pollub.f1data.Models.Data.Race;
import java.util.List;

/**
 * Repository for {@link Race}
 */
public interface RaceRepository extends JpaRepository<Race, Integer> {

    /**
     * This method returns a list of races on a circuit with a given id.
     * @param circuitId circuit id
     * @return list of races, can be empty
     */
    List<Race> getByCircuitId(Integer circuitId);

    /**
     * This method returns a list of races in a given year.
     * @param year year
     * @return list of races, can be empty
     */
    @Query("select r from Race r where r.year.id = :year order by r.round")
    List<Race> findAllByYear(@Param("year") Integer year);
}