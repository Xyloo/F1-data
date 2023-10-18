package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Driver;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
}