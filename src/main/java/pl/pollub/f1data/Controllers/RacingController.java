package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Services.RacingService;
import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;

@RestController
@RequestMapping("api/racing")
public class RacingController {

    @Autowired
    RacingService racingService;

    @GetMapping("/{raceId}/best-time")
    public ResponseEntity<DriverBestTimeDto> getBestRaceTimeByRaceId(@PathVariable Integer raceId) {
        DriverBestTimeDto bestTime = racingService.getBestRaceTimeByRaceId(raceId).orElse(null);
        if (bestTime != null) {
            return ResponseEntity.ok(bestTime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{raceId}/average-time")
    public ResponseEntity<String> getAverageRaceTime(@PathVariable Integer raceId) {
        String averageTime = racingService.getAverageRaceTime(raceId);
        if (averageTime != null) {
            return ResponseEntity.ok(averageTime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{raceId}/pitstops")
    public ResponseEntity<?> getPitstopsCountByLapForRace(@PathVariable Integer raceId) {
        return ResponseEntity.ok(racingService.getPitstopsCountByLapForRace(raceId));
    }

    @GetMapping("/{circuitId}/summary/best-times")
    public ResponseEntity<CircuitSummaryDto> getBestRaceTimesByCircuit(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = racingService.getBestRaceTimesByCircuitId(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{circuitId}/summary/average-lap-times")
    public ResponseEntity<CircuitSummaryDto> getAverageRaceTimesByCircuit(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = racingService.getAverageTimeByCircuitId(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{circuitId}/summary/pitstops")
    public ResponseEntity<CircuitSummaryDto> getAllPitstopsByCircuit(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = racingService.getAllPitstopsByCircuitId(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
