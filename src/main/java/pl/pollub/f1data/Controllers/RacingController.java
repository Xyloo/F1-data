package pl.pollub.f1data.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Services.RaceService;

import java.util.Optional;

@RestController
@RequestMapping("api/race")
public class RacingController {

    @Autowired
    RaceService raceService;

    @GetMapping("/{raceId}/best-time")
    public ResponseEntity<DriverBestTimeDto> getBestRaceTimeByRaceId(@PathVariable Integer raceId) {
        Optional<DriverBestTimeDto> bestTime = raceService.getBestRaceTimeByRaceId(raceId);
        if (bestTime != null) {
            return ResponseEntity.ok(bestTime.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{raceId}/average-time")
    public ResponseEntity<String> getAverageRaceTime(@PathVariable Integer raceId) {
        String averageTime = raceService.getAverageRaceTime(raceId);
        if (averageTime != null) {
            return ResponseEntity.ok(averageTime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{raceId}/pitstops")
    public ResponseEntity<?> getPitstopsCountByLapForRace(@PathVariable Integer raceId) {
        return ResponseEntity.ok(raceService.getPitstopsCountByLapForRace(raceId));
    }

    @GetMapping("/{circuitId}/summary/best-times")
    public ResponseEntity<CircuitSummaryDto> getBestRaceTimesByCircuit(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = raceService.getBestRaceTimesByCircuitId(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{circuitId}/summary/average-lap-times")
    public ResponseEntity<CircuitSummaryDto> getAverageRaceTimesByCircuit(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = raceService.getAverageTimeByCircuitId(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{circuitId}/summary/pitstops")
    public ResponseEntity<CircuitSummaryDto> getAllPitstopsByCircuit(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = raceService.getAllPitstopsByCircuitId(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }





}
