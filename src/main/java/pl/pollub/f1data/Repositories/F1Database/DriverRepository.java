package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pollub.f1data.Models.Data.Driver;

import java.util.List;

/**
 * Repository for {@link Driver}
 */
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    /**
     * This method returns drivers with a given nationality.
     * @param nationality nationality of the drivers
     * @return list of drivers with given nationality, can be empty
     */
    List<Driver> findByNationality(String nationality);

}