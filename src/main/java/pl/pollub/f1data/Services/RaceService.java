package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Models.Data.Result;

import java.util.Map;
import java.util.Optional;

public interface RaceService {

    Optional<DriverBestTimeDto> getBestRaceTimeByRaceId(Integer raceId);
    Optional<DriverBestTimeDto> mapResultToDriverBestTimeDto(Result bestResult);
    String getAverageRaceTime(Integer raceId);
    Map<Integer, Long> getPitstopsCountByLapForRace(Integer raceId);
}
