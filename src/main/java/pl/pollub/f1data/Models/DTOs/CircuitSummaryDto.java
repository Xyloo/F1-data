package pl.pollub.f1data.Models.DTOs;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Circuit summary data transfer object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CircuitSummaryDto {

    /**
     * Circuit id
     */
    public Integer circuitId;
    /**
     * Circuit name
     */
    public String name;
    /**
     * Circuit external reference
     */
    public String circuitRef;
    /**
     * Circuit location (city)
     */
    public String location;
    /**
     * Circuit country
     */
    public String country;
    /**
     * List of races with their summary
     */
    public List<RaceSummaryDto> races = new ArrayList<>();

    /**
     * Constructor for CircuitSummaryDto
     * @param circuitId circuit id
     * @param name circuit name
     * @param circuitRef circuit external reference
     * @param location circuit location (city)
     * @param country circuit country
     */
    public CircuitSummaryDto(Integer circuitId, String name, String circuitRef, String location, String country) {
        this.circuitId = circuitId;
        this.name = name;
        this.circuitRef = circuitRef;
        this.location = location;
        this.country = country;
    }
}

