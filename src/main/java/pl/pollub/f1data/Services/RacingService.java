package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Models.Data.Result;

import java.util.Map;
import java.util.Optional;

/**
 * Helper service for queries related to racing data (best time, average time, pitstops)
 */
public interface RacingService {

    /**
     * This method returns the best time and driver for a given race.
     * @param raceId race id
     * @return {@link DriverBestTimeDto} if data was found, empty optional otherwise
     */
    Optional<DriverBestTimeDto> getBestRaceTimeByRaceId(Integer raceId);

    /**
     * This method maps the race result to {@link DriverBestTimeDto}
     * @param bestResult best time result
     * @return {@link DriverBestTimeDto} if data was found, empty optional otherwise
     */
    Optional<DriverBestTimeDto> mapResultToDriverBestTimeDto(Optional<Result> bestResult);

    /**
     * This method returns the average race time of all drivers in a given race.
     * @param raceId race id
     * @return string with average time if data was found, empty string otherwise
     */
    String getAverageRaceTime(Integer raceId);

    /**
     * This method returns a map of pitstop count in certain laps in a given race.
     * @param raceId race id
     * @return map of pitstop count in certain laps, can be empty
     */
    Map<Integer, Long> getPitstopsCountByLapForRace(Integer raceId);

}
