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
    public List<RaceSummaryDto> races = new ArrayList<>();

    public CircuitSummaryDto(Integer circuitId, String name, String circuitRef) {
        this.circuitId = circuitId;
        this.name = name;
        this.circuitRef = circuitRef;
    }
}

