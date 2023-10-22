package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class RaceServiceImpl implements RaceService {

    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private RaceMapper raceMapper;
    @Autowired
    private CircuitMapper circuitMapper;
    @Autowired
    private ResultMapper resultMapper;

    @Override
    public List<RaceDto> getAllRacesByYear(Integer year) {
        List<RaceDto> raceDtosList = new ArrayList<>();
        List<Race> races = raceRepository.findAllByYear(year);

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
