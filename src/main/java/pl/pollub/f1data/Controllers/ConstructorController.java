package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Models.DTOs.ConstructorResultsDto;
import pl.pollub.f1data.Models.DTOs.ConstructorYearSummaryDto;
import pl.pollub.f1data.Models.Data.Constructor;
import pl.pollub.f1data.Services.ConstructorService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/constructor")
public class ConstructorController {

    @Autowired
    private ConstructorService constructorService;

    @GetMapping("/{constructorId}")
    ResponseEntity<Constructor> getConstructorById(@PathVariable Integer constructorId) {
        Optional<Constructor> constructor = constructorService.getConstructorById(constructorId);
        return constructor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    ResponseEntity<List<Constructor>> getAllConstructors() {
        List<Constructor> allConstructors = constructorService.getAllConstructors();
        if (allConstructors.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(allConstructors);
    }

    @GetMapping("/nationality/{nationality}")
    ResponseEntity<List<Constructor>> getAllConstructorsByNationality(@PathVariable String nationality){
        List<Constructor> allConstructors = constructorService.getAllConstructorsByNationality(nationality);
        if (allConstructors.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(allConstructors);
    }

    @GetMapping("/results/{constructorId}/{year}")
    ResponseEntity<List<ConstructorResultsDto>> getConstructorResultsByConstructorIdAndYear(@PathVariable Integer constructorId, @PathVariable int year){
        List<ConstructorResultsDto> constructorResultsDtoList = constructorService.getConstructorResultsByConstructorIdAndYear(constructorId, year);
        if (constructorResultsDtoList.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(constructorResultsDtoList);
    }

    @GetMapping("/results/{constructorId}")
    ResponseEntity<List<ConstructorYearSummaryDto>> getConstructorResultsByConstructorId(@PathVariable Integer constructorId){
        List<ConstructorYearSummaryDto> constructorResultsDtoList = constructorService.getConstructorStandingsByConstructorId(constructorId);
        if (constructorResultsDtoList.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(constructorResultsDtoList);
    }

    @DeleteMapping("/{constructorId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity deleteConstructorById(@PathVariable Integer constructorId){
        Optional<Constructor> constructor = constructorService.getConstructorById(constructorId);
        if (constructor.isPresent()) {
            constructorService.deleteConstructorById(constructorId);
            return ResponseEntity.ok(constructor);
        }
        return ResponseEntity.notFound().build();
    }

}
