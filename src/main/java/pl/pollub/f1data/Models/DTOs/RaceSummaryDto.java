package pl.pollub.f1data.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Race summary data transfer object
 */
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

    /**
     * Constructor
     * @param raceId id
     * @param name race name
     * @param year race year
     */
    public RaceSummaryDto(Integer raceId, String name, Integer year) {
        this.raceId = raceId;
        this.name = name;
        this.year = year;
    }

    Map<Integer, Long> lapPitstopMap;
}
