package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Laptime;
import pl.pollub.f1data.Models.Data.LaptimeId;
import java.util.List;
import java.util.Optional;

public interface LaptimeRepository extends JpaRepository<Laptime, LaptimeId> {

    public List<Laptime> getLapTimesByRaceId(Integer raceId);
}