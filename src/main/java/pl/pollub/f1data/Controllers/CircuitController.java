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

/**
 * This class is responsible for handling requests related to circuits.
 */
@RestController
@RequestMapping("api/circuits")
public class CircuitController {
    @Autowired
    private CircuitService circuitService;

    /**
     * This method returns three most recent races for a given circuit if the user is not authenticated.
     * It does not filter data if the user is authenticated.
     * @param userDetails the user that is currently logged in
     * @param circuitSummary the circuit summary
     * @return filtered circuit summary
     */
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

    /**
     * This endpoint returns a circuit summary with best times of each race on a given circuit.
     * Data is filtered if the user is not authenticated.
     * @see CircuitController#filterData(UserDetails, CircuitSummaryDto)
     * @param circuitId the id of the circuit
     * @param userDetails the user that is currently logged in
     * @return <p>• HTTP 200 with circuit summary if circuit exists</p>
     *       <p>• HTTP 404 if circuit does not exist</p>
     */
    @GetMapping("/{circuitId}/summary/best-times")
    public ResponseEntity<CircuitSummaryDto> getBestRaceTimesByCircuit(@PathVariable Integer circuitId, @AuthenticationPrincipal UserDetails userDetails) {
        CircuitSummaryDto circuitSummary = filterData(userDetails, circuitService.getBestRaceTimesByCircuitId(circuitId));
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This endpoint returns a circuit summary with average times of each race on a given circuit.
     * Data is filtered if the user is not authenticated.
     * @see CircuitController#filterData(UserDetails, CircuitSummaryDto)
     * @param circuitId the id of the circuit
     * @param userDetails the user that is currently logged in
     * @return <p>• HTTP 200 with circuit summary if circuit exists</p>
     *      <p>• HTTP 404 if circuit does not exist</p>
     */
    @GetMapping("/{circuitId}/summary/average-lap-times")
    public ResponseEntity<CircuitSummaryDto> getAverageRaceTimesByCircuit(@PathVariable Integer circuitId, @AuthenticationPrincipal UserDetails userDetails) {
        CircuitSummaryDto circuitSummary = filterData(userDetails, circuitService.getBestRaceTimesByCircuitId(circuitId));
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This endpoint returns a circuit summary with pitstops count for each lap on a given circuit.
     * Data is filtered if the user is not authenticated.
     * @see CircuitController#filterData(UserDetails, CircuitSummaryDto)
     * @param circuitId the id of the circuit
     * @param userDetails the user that is currently logged in
     * @return <p>• HTTP 200 with circuit summary if circuit exists</p>
     *     <p>• HTTP 404 if circuit does not exist</p>
     */
    @GetMapping("/{circuitId}/summary/pitstops")
    public ResponseEntity<CircuitSummaryDto> getAllPitstopsByCircuit(@PathVariable Integer circuitId, @AuthenticationPrincipal UserDetails userDetails) {
        CircuitSummaryDto circuitSummary = filterData(userDetails, circuitService.getAllPitstopsByCircuitId(circuitId));
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This endpoint returns a circuit summary with all data for a given circuit.
     * Data is filtered if the user is not authenticated.
     * @see CircuitController#filterData(UserDetails, CircuitSummaryDto)
     * @param circuitId the id of the circuit
     * @param userDetails the user that is currently logged in
     * @return <p>• HTTP 200 with circuit summary if circuit exists</p>
     *    <p>• HTTP 404 if circuit does not exist</p>
     */
    @GetMapping("/{circuitId}")
    public ResponseEntity<CircuitSummaryDto> getCircuitStats(@PathVariable Integer circuitId, @AuthenticationPrincipal UserDetails userDetails) {
        CircuitSummaryDto circuitSummary = filterData(userDetails, circuitService.getCircuitStats(circuitId));
        if (circuitSummary != null) {
            return ResponseEntity.ok(circuitSummary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This endpoint returns all circuits.
     * @return <p>• HTTP 200 with list of all circuits</p>
     */
    @GetMapping("/")
    public ResponseEntity<?> getAllCircuits() {
        return ResponseEntity.ok(circuitService.getAllCircuits());
    }

    /**
     * This endpoint deletes a circuit with a given id. It is only accessible for users with admin role.
     * @param circuitId the id of the circuit
     * @return <p>• HTTP 200 if circuit was deleted successfully</p>
     *     <p>• HTTP 404 if circuit does not exist</p>
     */
    @DeleteMapping("/{circuitId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCircuit(@PathVariable Integer circuitId) {
        int statusCode = circuitService.deleteCircuit(circuitId);
        return ResponseEntity.status(statusCode).build();
    }
}
