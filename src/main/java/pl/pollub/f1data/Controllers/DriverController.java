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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private DriverServiceImpl driverService;

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

}
