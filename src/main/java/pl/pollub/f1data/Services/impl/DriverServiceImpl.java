package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Models.Data.Driver;
import pl.pollub.f1data.Repositories.F1Database.DriverRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DriverServiceImpl implements pl.pollub.f1data.Services.DriverService{

    @Autowired
    private DriverRepository driverRepository;

    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Optional<Driver> getDriverById(Integer id) {
        return driverRepository.findById(id);
    }

    public List<Driver> getDriverByNationality(String nationality){
        return driverRepository.findByNationality(nationality);
    }

    public void deleteDriver(Integer id) {
        driverRepository.deleteById(id);
    }


}
