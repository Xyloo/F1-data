package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Sprintresult;

public interface SprintresultRepository extends JpaRepository<Sprintresult, Integer> {
}