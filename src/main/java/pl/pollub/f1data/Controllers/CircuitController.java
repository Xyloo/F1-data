package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Services.CircuitService;

@RestController
@RequestMapping("api/circuits")
public class CircuitController {
    @Autowired
    private CircuitService circuitService;

    @GetMapping("/{circuitId}/summary/best-times")
    public ResponseEntity<CircuitSummaryDto> getBestRaceTimesByCircuit(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = circuitService.getBestRaceTimesByCircuitId(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{circuitId}/summary/average-lap-times")
    public ResponseEntity<CircuitSummaryDto> getAverageRaceTimesByCircuit(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = circuitService.getAverageTimeByCircuitId(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{circuitId}/summary/pitstops")
    public ResponseEntity<CircuitSummaryDto> getAllPitstopsByCircuit(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = circuitService.getAllPitstopsByCircuitId(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{circuitId}")
    public ResponseEntity<CircuitSummaryDto> getCircuitStats(@PathVariable Integer circuitId) {
        CircuitSummaryDto circuitSummary = circuitService.getCircuitStats(circuitId);
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCircuits() {
        return ResponseEntity.ok(circuitService.getAllCircuits());
    }
}
