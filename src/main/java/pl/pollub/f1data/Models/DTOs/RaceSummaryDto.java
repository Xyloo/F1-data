package pl.pollub.f1data.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RaceSummaryDto {
    Integer raceId;
    String name;
    Integer year;
    DriverBestTimeDto bestLapTime;
    String averageLapTime;

    public RaceSummaryDto(Integer raceId, String name, Integer year) {
        this.raceId = raceId;
        this.name = name;
        this.year = year;
    }

    Map<Integer, Long> lapPitstopMap;
}
