package pl.pollub.f1data.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Constructor results data transfer object
 */
@Getter
@Setter
@AllArgsConstructor
public class ConstructorResultsDto {
    /**
     * Constructor results id
     */
    public int constructorStandingsId;
    /**
     * Constructor id
     */
    public int constructorId;
    /**
     * Race id
     */
    public int raceId;
    /**
     * Circuit name
     */
    public String circuitName;
    /**
     * Points
     */
    public float points;
    /**
     * Position
     */
    public int position;
}
