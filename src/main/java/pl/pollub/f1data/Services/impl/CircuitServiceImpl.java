package pl.pollub.f1data.Services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Models.DTOs.CircuitSummaryDto;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Models.DTOs.RaceSummaryDto;
import pl.pollub.f1data.Models.Data.Circuit;
import pl.pollub.f1data.Models.Data.Race;
import pl.pollub.f1data.Repositories.F1Database.CircuitRepository;
import pl.pollub.f1data.Repositories.F1Database.RaceRepository;
import pl.pollub.f1data.Services.CircuitService;
import pl.pollub.f1data.Services.RacingService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CircuitServiceImpl implements CircuitService {
    @Autowired
    private CircuitRepository circuitRepository;
    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private RacingService racingService;

    private static final Logger logger = LoggerFactory.getLogger(CircuitServiceImpl.class);
    @Override
    public CircuitSummaryDto getCircuitStats(Integer circuitId) {
        Circuit circuit = circuitRepository.findById(circuitId).orElse(null);
        if (circuit == null) return null;

        List<Race> raceList = raceRepository.getByCircuitId(circuitId);
        CircuitSummaryDto circuitSummaryDto = new CircuitSummaryDto(circuitId, circuit.getName(), circuit.getCircuitRef(), circuit.getLocation(), circuit.getCountry());
        for (Race race: raceList) {
            DriverBestTimeDto driverDto = racingService.getBestRaceTimeByRaceId(race.getId()).orElse(null);
            String averageRaceTime = racingService.getAverageRaceTime(race.getId());
            Map<Integer, Long> pitstopsMap = racingService.getPitstopsCountByLapForRace(race.getId());
            logger.info("Pitstops map: " + pitstopsMap.toString());

            RaceSummaryDto raceDto = new RaceSummaryDto(race.getId(), race.getName(), race.getYear().getId());
            raceDto.setBestLapTime(driverDto);
            raceDto.setAverageLapTime(averageRaceTime);
            raceDto.setLapPitstopMap(pitstopsMap);
            circuitSummaryDto.races.add(raceDto);
        }
        return circuitSummaryDto;
    }

    @Override
    public CircuitSummaryDto getBestRaceTimesByCircuitId(Integer circuitId) {
        Circuit circuit = circuitRepository.findById(circuitId).orElse(null);
        if (circuit == null) return null;

        List<Race> raceList = raceRepository.getByCircuitId(circuitId);
        CircuitSummaryDto circuitSummaryDto = new CircuitSummaryDto(circuitId, circuit.getName(), circuit.getCircuitRef(), circuit.getLocation(), circuit.getCountry());
        for (Race race : raceList) {
            Optional<DriverBestTimeDto> driverDto = racingService.getBestRaceTimeByRaceId(race.getId());
            RaceSummaryDto raceDto = new RaceSummaryDto(race.getId(), race.getName(), race.getYear().getId());
            raceDto.setBestLapTime(driverDto.orElse(null)); //or new object?
            circuitSummaryDto.races.add(raceDto);
        }
        return circuitSummaryDto;
    }

    @Override
    public CircuitSummaryDto getAverageTimeByCircuitId(Integer circuitId){
        Circuit circuit = circuitRepository.findById(circuitId).orElse(null);
        if (circuit == null) return null;

        List<Race> raceList = raceRepository.getByCircuitId(circuitId);
        CircuitSummaryDto circuitSummaryDto = new CircuitSummaryDto(circuitId, circuit.getName(), circuit.getCircuitRef(), circuit.getLocation(), circuit.getCountry());
        for (Race race : raceList) {
            String averageRaceTime = racingService.getAverageRaceTime(race.getId());
            RaceSummaryDto raceDto = new RaceSummaryDto(race.getId(), race.getName(), race.getYear().getId());
            raceDto.setAverageLapTime(averageRaceTime); //or new object?
            circuitSummaryDto.races.add(raceDto);
        }
        return circuitSummaryDto;
    }

    @Override
    public CircuitSummaryDto getAllPitstopsByCircuitId(Integer circuitId){
        Circuit circuit = circuitRepository.findById(circuitId).orElse(null);
        if (circuit == null) return null;

        List<Race> raceList = raceRepository.getByCircuitId(circuitId);
        CircuitSummaryDto circuitSummaryDto = new CircuitSummaryDto(circuitId, circuit.getName(), circuit.getCircuitRef(), circuit.getLocation(), circuit.getCountry());
        for (Race race : raceList) {
            Map<Integer, Long> pitstopsMap = racingService.getPitstopsCountByLapForRace(race.getId()) ;
            RaceSummaryDto raceDto = new RaceSummaryDto(race.getId(), race.getName(), race.getYear().getId());
            raceDto.setLapPitstopMap(pitstopsMap); //or new object?
            circuitSummaryDto.races.add(raceDto);
        }
        return circuitSummaryDto;
    }

    @Override
    public List<Circuit> getAllCircuits() {
        return circuitRepository.findAll();
    }

    @Override
    public int deleteCircuit(Integer circuitId) {
        Circuit circuit = circuitRepository.findById(circuitId).orElse(null);
        if (circuit == null) return 400;
        circuitRepository.deleteById(circuitId);
        return 200;
    }
}
