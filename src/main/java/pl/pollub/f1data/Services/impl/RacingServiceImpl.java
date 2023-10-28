package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Models.DTOs.RaceSummaryDto;
import pl.pollub.f1data.Models.Data.*;
import pl.pollub.f1data.Repositories.F1Database.*;
import pl.pollub.f1data.Services.RacingService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pl.pollub.f1data.Utils.TimeUtils;

@Service
public class RacingServiceImpl implements RacingService {

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



    @Override
    public Optional<DriverBestTimeDto> getBestRaceTimeByRaceId(Integer raceId) {
        Optional<Result> bestResult = resultRepository.findFastestLapByRaceId(raceId);
        if(bestResult.isEmpty()) return Optional.empty();
        return mapResultToDriverBestTimeDto(bestResult);
    }

    @Override
    public Optional<DriverBestTimeDto> mapResultToDriverBestTimeDto(Optional<Result> bestResult) {
        if (bestResult.isEmpty()) {
            return Optional.empty();
        }

        Driver driver = bestResult.get().getDriver();
        DriverBestTimeDto driverBestTimeDto = new DriverBestTimeDto(
                driver.getForename(),
                driver.getSurname(),
                driver.getNumber(),
                bestResult.get().getFastestLapTime()
        );
        return Optional.of(driverBestTimeDto);
    }


    @Override
    public String getAverageRaceTime(Integer raceId) {

        List<Laptime> lapTimes = laptimeRepository.getLapTimesByRaceId(raceId);
        if(lapTimes == null || lapTimes.size() == 0) return "";
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
        Circuit circuit = circuitRepository.findById(circuitId).orElse(null);
        if(circuit == null) return null;

        List<Race> raceList = raceRepository.getByCircuitId(circuitId);
        CircuitSummaryDto circuitSummaryDto = new CircuitSummaryDto(circuitId, circuit.getName(), circuit.getCircuitRef(), circuit.getLocation(), circuit.getCountry());
        for (Race race : raceList) {
            Optional<DriverBestTimeDto> driverDto =  getBestRaceTimeByRaceId(race.getId());
            RaceSummaryDto raceDto = new RaceSummaryDto(race.getId(), race.getName(), race.getYear().getId());
            raceDto.setBestLapTime(driverDto.orElse(null)); //or new object?
            circuitSummaryDto.races.add(raceDto);
        }
        return circuitSummaryDto;
    }

    @Override
    public CircuitSummaryDto getAverageTimeByCircuitId(Integer circuitId){
        Circuit circuit = circuitRepository.findById(circuitId).orElse(null);
        if(circuit == null) return null;

        List<Race> raceList = raceRepository.getByCircuitId(circuitId);
        CircuitSummaryDto circuitSummaryDto = new CircuitSummaryDto(circuitId, circuit.getName(), circuit.getCircuitRef(), circuit.getLocation(), circuit.getCountry());
        for (Race race : raceList) {
            String averageRaceTime =  getAverageRaceTime(race.getId());
            RaceSummaryDto raceDto = new RaceSummaryDto(race.getId(), race.getName(), race.getYear().getId());
            raceDto.setAverageLapTime(averageRaceTime); //or new object?
            circuitSummaryDto.races.add(raceDto);
        }
        return circuitSummaryDto;
    }

    @Override
    public CircuitSummaryDto getAllPitstopsByCircuitId(Integer circuitId){
        Circuit circuit = circuitRepository.findById(circuitId).orElse(null);
        if(circuit == null) return null;

        List<Race> raceList = raceRepository.getByCircuitId(circuitId);
        CircuitSummaryDto circuitSummaryDto = new CircuitSummaryDto(circuitId, circuit.getName(), circuit.getCircuitRef(), circuit.getLocation(), circuit.getCountry());
        for (Race race : raceList) {
            Map<Integer, Long> pitstopsMap = getPitstopsCountByLapForRace(circuitId) ;
            RaceSummaryDto raceDto = new RaceSummaryDto(race.getId(), race.getName(), race.getYear().getId());
            raceDto.setLapPitstopMap(pitstopsMap); //or new object?
            circuitSummaryDto.races.add(raceDto);
        }
        return circuitSummaryDto;
    }

}
