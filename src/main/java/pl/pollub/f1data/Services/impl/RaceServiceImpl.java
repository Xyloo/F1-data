package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Models.Data.Driver;
import pl.pollub.f1data.Models.Data.Laptime;
import pl.pollub.f1data.Models.Data.Result;
import pl.pollub.f1data.Repositories.F1Database.LaptimeRepository;
import pl.pollub.f1data.Repositories.F1Database.PitstopRepository;
import pl.pollub.f1data.Mappers.CircuitMapper;
import pl.pollub.f1data.Mappers.RaceMapper;
import pl.pollub.f1data.Mappers.ResultMapper;
import pl.pollub.f1data.Models.DTOs.RaceDto;
import pl.pollub.f1data.Models.DTOs.ResultDto;
import pl.pollub.f1data.Models.Data.Race;
import pl.pollub.f1data.Models.Data.Result;
import pl.pollub.f1data.Repositories.F1Database.RaceRepository;
import pl.pollub.f1data.Repositories.F1Database.ResultRepository;
import pl.pollub.f1data.Services.RaceService;
import pl.pollub.f1data.Utils.TimeUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
public class RaceServiceImpl implements RaceService {

    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private LaptimeRepository laptimeRepository;
    @Autowired
    private RaceMapper raceMapper;
    @Autowired
    private CircuitMapper circuitMapper;
    @Autowired
    private ResultMapper resultMapper;

    @Override
    public Optional<DriverBestTimeDto> getBestRaceTimeByRaceId(Integer raceId) {
        Result bestResult = resultRepository.findFastestLapByRaceId(raceId).orElse(null);
        if(bestResult == null) return Optional.empty();
        return mapResultToDriverBestTimeDto(bestResult);
    }

    @Override
    public Optional<DriverBestTimeDto> mapResultToDriverBestTimeDto(Result bestResult) {
        if (bestResult == null) {
            return Optional.empty();
        }

        Driver driver = bestResult.getDriver();
        DriverBestTimeDto driverBestTimeDto = new DriverBestTimeDto(
                driver.getForename(),
                driver.getSurname(),
                driver.getNumber(),
                bestResult.getFastestLapTime()
        );
        return Optional.of(driverBestTimeDto);
    }


    @Override
    public List<RaceDto> getAllRacesByYear(Integer year) {
        List<RaceDto> raceDtosList = new ArrayList<>();
        List<Race> races = raceRepository.findAllByYear(year);

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
        for(Race race : races){
            raceDtosList.add(raceMapper.toDto(race));
        }
        return raceDtosList;
    }//race id year round circuitDto name date time url

    @Override
    public List<ResultDto> getRaceResultsByRaceId(Integer raceId){
        List<Result> results = resultRepository.findByRaceId(raceId);
        List<ResultDto> resultDtos = new ArrayList<>();

        for(Result result : results){
            resultDtos.add(resultMapper.toDto(result));
        }
        return resultDtos;
    }
    //driver forename surname, contructor name, grid, positionOrder, points, time, statusDTO

}
