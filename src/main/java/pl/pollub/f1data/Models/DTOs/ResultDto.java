package pl.pollub.f1data.Models.DTOs;

/**
 * Result data transfer object
 */
public class ResultDto {

    /**
     * Id of the result
     */
    public Integer id;
    /**
     * Driver's name
     */
    public String driverName;
    /**
     * Constructor's name
     */
    public String constructorName; //name
    /**
     * Grid position
     */
    public Integer grid; //grid position
    /**
     * Position
     */
    public Integer positionOrder; //position
    /**
     * Points
     */
    public Float points;
    /**
     * Time
     */
    public String time;
    /**
     * Status
     * @see pl.pollub.f1data.Models.Data.Status
     */
    public String statusDetails;
}
