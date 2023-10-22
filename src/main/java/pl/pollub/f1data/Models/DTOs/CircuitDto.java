package pl.pollub.f1data.Models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CircuitDto {

    public Integer id;
    public String name;
    public String location;
    public String country;
    public String url;
}
