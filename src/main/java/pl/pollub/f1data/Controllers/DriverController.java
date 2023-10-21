package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.pollub.f1data.Mappers.AutoMapper;
import pl.pollub.f1data.Models.DTOs.DriverDto;
import pl.pollub.f1data.Models.Data.Driver;
import pl.pollub.f1data.Models.MessageResponse;
import pl.pollub.f1data.Services.impl.DriverServiceImpl;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private DriverServiceImpl driverService;
    @Autowired
    private AutoMapper autoMapper;

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDriverById(@PathVariable Integer id) {
        Optional<Driver> driverOptional = driverService.getDriverById(id);
        if (driverOptional.isPresent()) {
            return ResponseEntity.ok(driverOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Driver with ID " + id + " not found"));
        }
    }

    //get by nationality
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<Driver>> getDriversByNationality(@PathVariable String nationality){
        List<Driver> result = driverService.getDriverByNationality(nationality);
        if(result != null){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createDriver(@RequestBody DriverDto driverDto) {
        try {
            Driver driver = autoMapper.mapToDriver(driverDto);
            Driver savedDriver = driverService.saveDriver(driver);
            if (savedDriver != null) {
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedDriver.getId())
                        .toUri();
                return ResponseEntity.created(location).body(savedDriver);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to save driver");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(@PathVariable Integer id, @RequestBody DriverDto driverDto) {
        Driver driver = autoMapper.mapToDriver(driverDto);

        if (driverService.getDriverById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        driver.setId(id);
        Driver updatedDriver = driverService.saveDriver(driver);
        return ResponseEntity.ok(updatedDriver); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDriverById(@PathVariable Integer id) {
        try {
            driverService.deleteDriver(id);
            return ResponseEntity.ok("Driver with ID " + id + " deleted successfully");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Driver with ID " + id + " not found");
        }
    }


}
