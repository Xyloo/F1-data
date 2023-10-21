package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Models.DTOs.RaceSummaryDto;
import pl.pollub.f1data.Models.Data.Circuit;
import pl.pollub.f1data.Models.Data.Laptime;
import pl.pollub.f1data.Models.Data.Race;
import pl.pollub.f1data.Models.Data.Result;
import pl.pollub.f1data.Repositories.F1Database.*;
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
    @Autowired
    private CircuitRepository circuitRepository;
    @Autowired
    private RaceRepository raceRepository;



    public Optional<DriverBestTimeDto> getBestRaceTimeByRaceId(Integer raceId) {
        Optional<Result> bestResult = resultRepository.findFastestLapByRaceId(raceId);
        if(bestResult.isEmpty()) return Optional.empty();

        DriverBestTimeDto driverBestTimeDto = new DriverBestTimeDto(
                bestResult.get().getDriver().getForename(),
                bestResult.get().getDriver().getSurname(),
                bestResult.get().getDriver().getNumber(),
                bestResult.get().getFastestLapTime()
        );
        return Optional.of(driverBestTimeDto);
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

    @Override
    public CircuitSummaryDto getBestRaceTimesByCircuitId(Integer circuitId) {
        Optional<Circuit> circuit = circuitRepository.findById(circuitId);
        if(circuit.isEmpty()) return null;

        List<Race> raceList = raceRepository.getByCircuitId(circuitId);
        CircuitSummaryDto circuitSummaryDto = new CircuitSummaryDto(circuitId, circuit.get().getName(), circuit.get().getCircuitRef());

        for (Race race : raceList) {
            Optional<DriverBestTimeDto> driverDto =  getBestRaceTimeByRaceId(race.getId());
            RaceSummaryDto raceDto = new RaceSummaryDto(race.getId(), race.getName(), race.getYear().getId());
            raceDto.setBestLapTime(driverDto.orElse(null)); //or new object?
            circuitSummaryDto.races.add(raceDto);
        }
        return circuitSummaryDto;
    }
}
