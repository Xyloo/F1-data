package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Models.DTOs.ConstructorDto;
import pl.pollub.f1data.Models.DTOs.ConstructorResultsDto;
import pl.pollub.f1data.Models.DTOs.ConstructorYearSummaryDto;
import pl.pollub.f1data.Models.Data.Constructor;
import pl.pollub.f1data.Services.ConstructorService;
import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for handling requests related to constructors.
 */
@RestController
@RequestMapping("api/constructor")
public class ConstructorController {

    @Autowired
    private ConstructorService constructorService;

    /**
     * This endpoint returns a constructor with a given id.
     * @param constructorId the id of the constructor
     * @return <p>• HTTP 200 with constructor if constructor exists</p>
     *      <p>• HTTP 404 if constructor does not exist</p>
     */
    @GetMapping("/{constructorId}")
    ResponseEntity<Constructor> getConstructorById(@PathVariable Integer constructorId) {
        Optional<Constructor> constructor = constructorService.getConstructorById(constructorId);
        return constructor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * This endpoint returns all constructors.
     * @return <p>• HTTP 200 with list of constructors if there are any</p>
     *     <p>• HTTP 404 if there are no constructors</p>
     */
    @GetMapping("/")
    ResponseEntity<List<ConstructorDto>> getAllConstructors() {
        List<ConstructorDto> allConstructors = constructorService.getAllConstructors();
        if (allConstructors.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(allConstructors);
    }

    /**
     * This endpoint returns all constructors with a given nationality.
     * @param nationality the nationality of the constructor
     * @return <p>• HTTP 200 with list of constructors if there are any</p>
     *    <p>• HTTP 404 if there are no constructors</p>
     */
    @GetMapping("/nationality/{nationality}")
    ResponseEntity<List<ConstructorDto>> getAllConstructorsByNationality(@PathVariable String nationality){
        List<ConstructorDto> allConstructors = constructorService.getAllConstructorsByNationality(nationality);
        if (allConstructors.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(allConstructors);
    }

    /**
     * This endpoint returns constructor results for a given year.
     * @param constructorId the id of the constructor
     * @param year the year of the results
     * @return <p>• HTTP 200 with list of constructor results if there are any</p>
     *   <p>• HTTP 404 if there are no constructor results</p>
     */
    @GetMapping("/results/{constructorId}/{year}")
    ResponseEntity<List<ConstructorResultsDto>> getConstructorResultsByConstructorIdAndYear(@PathVariable Integer constructorId, @PathVariable int year){
        List<ConstructorResultsDto> constructorResultsDtoList = constructorService.getConstructorResultsByConstructorIdAndYear(constructorId, year);
        if (constructorResultsDtoList.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(constructorResultsDtoList);
    }

    /**
     * This endpoint returns constructor results for a given constructor.
     * @param constructorId the id of the constructor
     * @return <p>• HTTP 200 with list of constructor results if there are any</p>
     *  <p>• HTTP 404 if there are no constructor results</p>
     */
    @GetMapping("/results/{constructorId}")
    ResponseEntity<List<ConstructorYearSummaryDto>> getConstructorResultsByConstructorId(@PathVariable Integer constructorId){
        List<ConstructorYearSummaryDto> constructorResultsDtoList = constructorService.getConstructorStandingsByConstructorId(constructorId);
        if (constructorResultsDtoList.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(constructorResultsDtoList);
    }

    /**
     * This endpoint deletes constructor data with a given id. It is only accessible for users with admin role.
     * @param constructorId the id of the constructor
     * @return <p>• HTTP 200 with constructor if constructor was deleted successfully</p>
     *    <p>• HTTP 404 if constructor does not exist</p>
     */
    @DeleteMapping("/{constructorId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<?> deleteConstructorById(@PathVariable Integer constructorId){
        Optional<Constructor> constructor = constructorService.getConstructorById(constructorId);
        if (constructor.isPresent()) {
            constructorService.deleteConstructorById(constructorId);
            return ResponseEntity.ok(constructor);
        }
        return ResponseEntity.notFound().build();
    }

}
