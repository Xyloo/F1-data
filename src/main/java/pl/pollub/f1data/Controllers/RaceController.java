package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Models.DTOs.RaceDto;
import pl.pollub.f1data.Models.DTOs.ResultDto;
import pl.pollub.f1data.Services.RaceService;

import java.util.List;

@RestController
@RequestMapping("api/race")
public class RaceController {

    @Autowired
    RaceService raceService;

    @GetMapping("/{year}")
    public ResponseEntity<?> getAllRacesByYear(@PathVariable Integer year) {
        try {
            List<RaceDto> races = raceService.getAllRacesByYear(year);
            if (races.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(races);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/{raceId}/results")
    public ResponseEntity<?> getResultsByRaceId(@PathVariable Integer raceId) {
        try {
            List<ResultDto> results = raceService.getRaceResultsByRaceId(raceId);
            if (results.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


}
