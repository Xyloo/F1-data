package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.Data.Circuit;

import java.util.List;

/**
 * Circuit service interface
 */
public interface CircuitService {
    /**
     * Method for getting circuit data with best race times by circuit id
     * @param circuitId circuit id
     * @return {@link CircuitSummaryDto} if circuit exists, null otherwise
     */
    CircuitSummaryDto getBestRaceTimesByCircuitId(Integer circuitId);

    /**
     * Method for getting circuit data with average race times by circuit id
     * @param circuitId circuit id
     * @return {@link CircuitSummaryDto} if circuit exists, null otherwise
     */
    CircuitSummaryDto getAverageTimeByCircuitId(Integer circuitId);

    /**
     * Method for getting circuit data with all pitstops by circuit id
     * @param circuitId circuit id
     * @return {@link CircuitSummaryDto} if circuit exists, null otherwise
     */
    CircuitSummaryDto getAllPitstopsByCircuitId(Integer circuitId);

    /**
     * Method for getting circuit data with all data (best times, average times, pitstops) by circuit id
     * @param circuitId circuit id
     * @return {@link CircuitSummaryDto} if circuit exists, null otherwise
     */

    CircuitSummaryDto getCircuitStats(Integer circuitId);

    /**
     * Method for getting all circuits
     * @return list of {@link Circuit} objects
     */
    List<Circuit> getAllCircuits();

    /**
     * Method for deleting circuit by id
     * @param circuitId circuit id
     * @return 200 if circuit was deleted, 404 if circuit was not found
     */
    int deleteCircuit(Integer circuitId);
}
