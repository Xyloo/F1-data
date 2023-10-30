package pl.pollub.f1data.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Race data transfer object
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RaceDto {
    /**
     * Id
     */
    public Integer id;
    /**
     * Year
     */
    public Integer year;
    /**
     * Round
     */
    public Integer round;
    /**
     * Circuit details
     * @see CircuitDto
     */
    public CircuitDto circuitDetails;
    /**
     * Name
     */
    public String name;
    /**
     * Date
     */
    public String date;
    /**
     * Time
     */
    public String time;
    /**
     * Wikipedia race url
     */
    public String url;
}
