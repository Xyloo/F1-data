package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Models.Data.Driver;
import pl.pollub.f1data.Repositories.F1Database.DriverRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link Driver}
 */
@Service
public class DriverServiceImpl implements pl.pollub.f1data.Services.DriverService{

    @Autowired
    private DriverRepository driverRepository;

    /**
     * This method saves a driver to the database.
     * @param driver driver to be saved
     * @return saved driver
     */
    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    /**
     * This method returns all drivers.
     * @return list of drivers, can be empty
     */
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    /**
     * This method returns a driver with a given ID.
     * @param id id of the driver
     * @return optional of driver with given ID, can be empty
     */
    public Optional<Driver> getDriverById(Integer id) {
        return driverRepository.findById(id);
    }

    /**
     * This method returns a list of drivers with a given nationality.
     * @param nationality nationality of the drivers
     * @return list of drivers with given nationality, can be empty
     */
    public List<Driver> getDriverByNationality(String nationality){
        return driverRepository.findByNationality(nationality);
    }

    /**
     * This method deletes a driver with a given ID.
     * @param id id of the driver
     */

    public void deleteDriver(Integer id) {
        driverRepository.deleteById(id);
    }


}
