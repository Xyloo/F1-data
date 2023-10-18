package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Pitstop;
import pl.pollub.f1data.Models.Data.PitstopId;

public interface PitstopRepository extends JpaRepository<Pitstop, PitstopId> {
}