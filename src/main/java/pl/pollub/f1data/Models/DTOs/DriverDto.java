package pl.pollub.f1data.Models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import pl.pollub.f1data.Models.Data.Driver;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Driver}
 */
@Value
public class DriverDto implements Serializable {
    /**
     * Driver reference
     */
    @NotNull
    @Size(max = 255)
    String driverRef;
    /**
     * Driver's number
     */
    Integer number;
    /**
     * Driver's code
     */
    @Size(max = 3)
    String code;
    /**
     * Driver's forename
     */
    @NotNull
    @Size(max = 255)
    String forename;
    /**
     * Driver's surname
     */
    @NotNull
    @Size(max = 255)
    String surname;
    /**
     * Driver's date of birth
     */
    LocalDate dob;
    /**
     * Driver's nationality
     */
    @Size(max = 255)
    String nationality;
    /**
     * Driver's Wikipedia url
     */
    @NotNull
    @Size(max = 255)
    String url;
}