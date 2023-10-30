package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.ConstructorResultsDto;
import pl.pollub.f1data.Models.DTOs.ConstructorYearSummaryDto;
import pl.pollub.f1data.Models.Data.Constructor;
import java.util.List;
import java.util.Optional;

/**
 * This interface is responsible for handling requests related to constructors.
 */
public interface ConstructorService {

    /**
     * This method returns a constructor with a given id.
     * @param constructorId the id of the constructor
     * @return optional of constructor, can be an empty optional
     */
    Optional<Constructor> getConstructorById(Integer constructorId);

    /**
     * This method returns all constructors.
     * @return list of constructors, can be empty
     */
    List<Constructor> getAllConstructors();

    /**
     * This method returns all constructors with a given nationality.
     * @param nationality constructor nationality
     * @return list of constructors, can be empty
     */
    List<Constructor> getAllConstructorsByNationality(String nationality);

    /**
     * This method returns constructor results for a given constructor and year.
     * @param constructorId constructor id
     * @param year year of the results
     * @return list of constructor results, can be empty
     */
    List<ConstructorResultsDto> getConstructorResultsByConstructorIdAndYear(Integer constructorId, int year);

    /**
     * This method returns a list of constructor results for a given constructor.
     * @param constructorId constructor id
     * @return list of constructor results, can be empty
     */
    List<ConstructorYearSummaryDto> getConstructorStandingsByConstructorId(Integer constructorId);

    /**
     * This method deletes a constructor with a given id.
     * @param constructorId constructor id
     * @return optional of deleted constructor, can be an empty optional
     */
    Optional<Constructor> deleteConstructorById(Integer constructorId);

}
