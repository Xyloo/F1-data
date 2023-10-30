package pl.pollub.f1data.Models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Circuit data transfer object
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CircuitDto {

    /**
     * Circuit id
     */
    public Integer id;
    /**
     * Circuit name
     */
    public String name;
    /**
     * Circuit location (city)
     */
    public String location;
    /**
     * Circuit country
     */
    public String country;
    /**
     * Circuit Wikipedia url
     */
    public String url;
}
