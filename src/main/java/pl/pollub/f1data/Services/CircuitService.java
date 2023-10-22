package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.Data.Circuit;

import java.util.List;

public interface CircuitService {
    CircuitSummaryDto getBestRaceTimesByCircuitId(Integer circuitId);
    CircuitSummaryDto getAverageTimeByCircuitId(Integer circuitId);
    CircuitSummaryDto getAllPitstopsByCircuitId(Integer circuitId);

    CircuitSummaryDto getCircuitStats(Integer circuitId);
    List<Circuit> getAllCircuits();
}
