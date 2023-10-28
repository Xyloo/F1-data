package pl.pollub.f1data.Models.DTOs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConstructorYearSummaryDto {

    public int year;
    public String constructor_name;
    public double total_points;
}
