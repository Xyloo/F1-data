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
    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 3, max = 32, message = "Username must be between 3 and 32 characters long.")
    @Pattern(regexp = "^[a-zA-Z0-9-_]*$", message = "Username can only contain letters, numbers, dashes and underscores.")
    private String username;
    @NotBlank(message = "Email cannot be blank.")
    @Size(max = 100, message = "Email must be less than 100 characters long.")
    @Email(message = "Email must be valid.")
    private String email;
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters long.")
    private String password;
}
