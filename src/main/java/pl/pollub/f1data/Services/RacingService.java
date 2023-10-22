package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Models.Data.Result;

import java.util.Map;
import java.util.Optional;

public interface RacingService {

    Optional<DriverBestTimeDto> getBestRaceTimeByRaceId(Integer raceId);
    Optional<DriverBestTimeDto> mapResultToDriverBestTimeDto(Optional<Result> bestResult);
    String getAverageRaceTime(Integer raceId);
    Map<Integer, Long> getPitstopsCountByLapForRace(Integer raceId);
    CircuitSummaryDto getBestRaceTimesByCircuitId(Integer circuitId);
    CircuitSummaryDto getAverageTimeByCircuitId(Integer circuitId);
    CircuitSummaryDto getAllPitstopsByCircuitId(Integer circuitId);
}
