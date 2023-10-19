package pl.pollub.f1data.Models.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateUserDTO {
    @NotBlank
    @Size(min =3, max = 32)
    @Pattern(regexp = "^[a-zA-Z0-9-_]*$", message = "Username can only contain letters, numbers, dashes and underscores.")
    private String username;
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;
    @NotBlank
    @Size(min=6, max=100)
    private String password;
}
