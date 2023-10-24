package pl.pollub.f1data.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RaceDto {
    public Integer id;
    public Integer year;
    public Integer round;
    public CircuitDto circuitDetails;
    public String name;
    public String date;
    public String time;
    public String url;
}
