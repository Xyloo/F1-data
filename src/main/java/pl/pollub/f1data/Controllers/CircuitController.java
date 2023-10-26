package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.DTOs.RaceSummaryDto;
import pl.pollub.f1data.Services.CircuitService;

import java.util.List;

@RestController
@RequestMapping("api/circuits")
public class CircuitController {
    @Autowired
    private CircuitService circuitService;
    
    private CircuitSummaryDto filterData(UserDetails userDetails, CircuitSummaryDto circuitSummary) {
        if(circuitSummary == null)
            return null;
        if (userDetails != null) {
            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                return circuitSummary;
            }
        }
        //return only 3 most recent races
        List<RaceSummaryDto> races = circuitSummary.getRaces();
        int min = Math.min(races.size(), 3);
        races.sort((r1, r2) -> r2.getYear().compareTo(r1.getYear()));
        races = races.subList(0, min);
        circuitSummary.setRaces(races);
        return circuitSummary;
    }

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
    public ResponseEntity<CircuitSummaryDto> getCircuitStats(@PathVariable Integer circuitId, @AuthenticationPrincipal UserDetails userDetails) {
        CircuitSummaryDto circuitSummary = filterData(userDetails, circuitService.getCircuitStats(circuitId));
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

    @DeleteMapping("/{circuitId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCircuit(@PathVariable Integer circuitId) {
        int statusCode = circuitService.deleteCircuit(circuitId);
        return ResponseEntity.status(statusCode).build();
    }
}
