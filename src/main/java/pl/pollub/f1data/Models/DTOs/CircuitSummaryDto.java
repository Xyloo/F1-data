package pl.pollub.f1data.Models.DTOs;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CircuitSummaryDto {

    public Integer circuitId;
    public String name;
    public String circuitRef;
    public String location;
    public String country;
    public List<RaceSummaryDto> races = new ArrayList<>();

    public CircuitSummaryDto(Integer circuitId, String name, String circuitRef, String location, String country) {
        this.circuitId = circuitId;
        this.name = name;
        this.circuitRef = circuitRef;
        this.location = location;
        this.country = country;
    }
}

