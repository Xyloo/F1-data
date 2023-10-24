package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.RaceDto;
import pl.pollub.f1data.Models.DTOs.ResultDto;
import java.util.List;

public interface RaceService {
    List<RaceDto> getAllRacesByYear(Integer year);
    List<ResultDto> getRaceResultsByRaceId(Integer raceId);
}