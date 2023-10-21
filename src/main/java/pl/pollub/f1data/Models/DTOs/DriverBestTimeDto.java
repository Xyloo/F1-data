package pl.pollub.f1data.Models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DriverBestTimeDto {
    String forename;
    String surname;
    Integer number;
    String bestLapTime;
}
