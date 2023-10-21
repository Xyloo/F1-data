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
    @NotNull
    @Size(max = 255)
    String driverRef;
    Integer number;
    @Size(max = 3)
    String code;
    @NotNull
    @Size(max = 255)
    String forename;
    @NotNull
    @Size(max = 255)
    String surname;
    LocalDate dob;
    @Size(max = 255)
    String nationality;
    @NotNull
    @Size(max = 255)
    String url;
}