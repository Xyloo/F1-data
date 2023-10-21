package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Race;
import java.util.List;

public interface RaceRepository extends JpaRepository<Race, Integer> {

    List<Race> getByCircuitId(Integer circuitId);

}