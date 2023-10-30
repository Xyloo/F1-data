package pl.pollub.f1data.Models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Driver best time data transfer object
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverBestTimeDto {
    String forename;
    String surname;
    Integer number;
    String bestLapTime;
}
