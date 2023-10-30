package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Models.DTOs.RaceDto;
import pl.pollub.f1data.Models.DTOs.ResultDto;
import pl.pollub.f1data.Services.RaceService;

import java.util.List;

/**
 * This class is responsible for handling requests related to races.
 */
@RestController
@RequestMapping("api/race")
public class RaceController {

    @Autowired
    RaceService raceService;

    /**
     * This endpoint returns all races from a given year.
     * @param year year
     * @return <p>• HTTP 200 with list of races</p>
     * <p>• HTTP 204 if no races found</p>
     * <p>• HTTP 500 with an error message if an exception happens</p>
     */
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

    /**
     * This endpoint returns race results for a given race id.
     * @param raceId race id
     * @return <p>• HTTP 200 with list of results</p>
     * <p>• HTTP 204 if no results found</p>
     * <p>• HTTP 500 with an error message if an exception happens</p>
     */
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
