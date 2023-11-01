package pl.pollub.f1data.Models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConstructorDto {
    Integer id;
    @NotNull
    @Size(max = 255)
    String constructorRef;
    @NotNull
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String nationality;
    @NotNull
    @Size(max = 255)
    String url;
}