package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Models.Data.Laptime;
import pl.pollub.f1data.Models.Data.Result;
import pl.pollub.f1data.Repositories.F1Database.LaptimeRepository;
import pl.pollub.f1data.Repositories.F1Database.PitstopRepository;
import pl.pollub.f1data.Repositories.F1Database.ResultRepository;
import pl.pollub.f1data.Services.RaceService;

import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pl.pollub.f1data.Utils.TimeUtils;

@Service
public class RaceServiceImpl implements RaceService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private PitstopRepository pitstopRepository;
    @Autowired
    private LaptimeRepository laptimeRepository;

    public String getBestRaceTimeByRaceId(Integer raceId) {
        Optional<Result> bestResult = resultRepository.findTopByRaceIdOrderByTimeAsc(raceId);
        return bestResult != null ? bestResult.get().getFastestLapTime() : "";
    }

    @Override
    public String getAverageRaceTime(Integer raceId) {

        List<Laptime> lapTimes = laptimeRepository.getLapTimesByRaceId(raceId);
        if(lapTimes == null) return "";
        long totalMilliseconds = 0;

        for(Laptime laptime: lapTimes){
            totalMilliseconds += laptime.getMilliseconds();
        }
        return TimeUtils.convertToLapTimeFormat(totalMilliseconds/lapTimes.size());
    }

    @Override
    public Map<Integer, Long> getPitstopsCountByLapForRace(Integer raceId) {
        List<Object[]> rawData = pitstopRepository.findPitstopsByRaceGroupedByLap(raceId);
        Map<Integer, Long> resultMap = new LinkedHashMap<>();
        for (Object[] entry : rawData) {
            resultMap.put((Integer) entry[0], (Long) entry[1]);
        }
        return resultMap;
    }


}
