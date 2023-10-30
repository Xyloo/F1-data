package pl.pollub.f1data.Models.DTOs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Constructor year summary data transfer object
 */
@Getter
@Setter
@AllArgsConstructor
public class ConstructorYearSummaryDto {

    /**
     * Year
     */
    public int year;
    /**
     * Constructor name
     */
    public String constructor_name;
    /**
     * Total points in given year
     */
    public double total_points;
}
