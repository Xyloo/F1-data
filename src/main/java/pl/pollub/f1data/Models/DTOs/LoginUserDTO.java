package pl.pollub.f1data.Models.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
