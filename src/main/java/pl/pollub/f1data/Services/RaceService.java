package pl.pollub.f1data.Services;

import java.util.List;
import java.util.Map;

public interface RaceService {

    String getBestRaceTimeByRaceId(Integer raceId);
    String getAverageRaceTime(Integer raceId);
    Map<Integer, Long> getPitstopsCountByLapForRace(Integer raceId);
}
