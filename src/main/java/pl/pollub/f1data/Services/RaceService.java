package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RaceService {

    Optional<DriverBestTimeDto> getBestRaceTimeByRaceId(Integer raceId);
    String getAverageRaceTime(Integer raceId);
    Map<Integer, Long> getPitstopsCountByLapForRace(Integer raceId);
    CircuitSummaryDto getBestRaceTimesByCircuitId(Integer circuitId);
}
