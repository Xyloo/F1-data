package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Models.Data.Driver;
import pl.pollub.f1data.Models.MessageResponse;
import pl.pollub.f1data.Services.impl.DriverServiceImpl;

import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for handling requests related to drivers.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private DriverServiceImpl driverService;

    /**
     * This endpoint returns all drivers.
     * @return <p>• HTTP 200 with list of drivers</p>
     *       <p>• HTTP 404 if no drivers found</p>
     */
    @GetMapping
    public ResponseEntity<?> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();
        if (drivers != null && !drivers.isEmpty()) {
            return ResponseEntity.ok(drivers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("No drivers found"));
        }
    }

    /**
     * This endpoint returns a driver with a given ID.
     * @param id the id of the driver
     * @return <p>• HTTP 200 with driver if driver exists</p>
     *      <p>• HTTP 404 if driver does not exist</p>
     */
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

    /**
     * This endpoint returns drivers with a given nationality.
     * @param nationality nationality of the driver
     * @return <p>• HTTP 200 with list of drivers if such drivers exist</p>
     *     <p>• HTTP 404 if no drivers found</p>
     */
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<Driver>> getDriversByNationality(@PathVariable String nationality){
        List<Driver> result = driverService.getDriverByNationality(nationality);
        if(result != null){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}
