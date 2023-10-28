package pl.pollub.f1data.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConstructorResultsDto {
    public int constructorStandingsId;
    public int constructorId;
    public int raceId;
    public String circuitName;
    public float points;
    public int position;
}
