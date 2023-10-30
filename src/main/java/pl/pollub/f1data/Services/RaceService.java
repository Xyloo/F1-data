package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.RaceDto;
import pl.pollub.f1data.Models.DTOs.ResultDto;
import java.util.List;

/**
 * Service for {@link pl.pollub.f1data.Models.Data.Race} and {@link pl.pollub.f1data.Models.Data.Result}
 */
public interface RaceService {
    /**
     * This method returns a list of races in a given year.
     * @param year year
     * @return list of races, can be empty
     */
    List<RaceDto> getAllRacesByYear(Integer year);

    /**
     * This method returns results of a race with given id.
     * @param raceId race id
     * @return list of results, can be empty
     */
    List<ResultDto> getRaceResultsByRaceId(Integer raceId);
}