package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Result;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Integer> {

    public Optional<Result> findTopByRaceIdOrderByTimeAsc(Integer raceId);


}