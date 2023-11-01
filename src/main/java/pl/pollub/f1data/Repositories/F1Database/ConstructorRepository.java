package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pollub.f1data.Models.DTOs.ConstructorDto;
import pl.pollub.f1data.Models.DTOs.ConstructorResultsDto;
import pl.pollub.f1data.Models.DTOs.ConstructorYearSummaryDto;
import pl.pollub.f1data.Models.Data.Constructor;

import java.util.List;

/**
 * This interface is responsible for handling queries related to constructors.
 */
public interface ConstructorRepository extends JpaRepository<Constructor, Integer> {

    //** This method returns a list of constructors with their ids, names, nationalities and urls. */
    @Query("SELECT new pl.pollub.f1data.Models.DTOs.ConstructorDto(c.id, c.constructorRef, c.name, c.nationality, c.url) FROM Constructor c")
    List<ConstructorDto> findAllConstructors();

    /**
     * This method returns a constructor with a given nationality.
     * @param nationality nationality of the constructor
     * @return list of constructors that satisfy the given nationality, can be empty
     */
    @Query("SELECT new pl.pollub.f1data.Models.DTOs.ConstructorDto(c.id, c.constructorRef, c.name, c.nationality, c.url) FROM Constructor c where c.nationality = ?1")
    List<ConstructorDto> findConstructorByNationality(String nationality);

    /**
     * This method returns a list of constructor results for a given constructor and year.
     * @param constructorId the id of the constructor
     * @param year the year of the results
     * @return list of constructor results, can be empty
     */
    @Query("SELECT new pl.pollub.f1data.Models.DTOs.ConstructorResultsDto(cs.id, cs.constructor.id, cs.race.id, cs.race.circuit.name, cs.points, cs.position) " +
            "FROM Constructorstanding cs " +
            "JOIN Constructor c ON cs.constructor.id = c.id " +
            "WHERE cs.constructor.id = ?1 and cs.race.year.id = ?2")
    List<ConstructorResultsDto> getConstructorResultsByConstructorIdAndYear(Integer constructorId, Integer year);

    /**
     * This method returns a list of constructor results for a given constructor.
     * @param constructorId the id of the constructor
     * @return list of constructor results, can be empty
     */
    @Query("SELECT new pl.pollub.f1data.Models.DTOs.ConstructorYearSummaryDto(s.id, c.name, SUM(cs.points)) " +
            "FROM Season s join Race r on r.year.id = s.id " +
            "join Constructorstanding cs on r.id = cs.race.id " +
            "join Constructor c on c.id = cs.constructor.id " +
            "WHERE c.id = ?1 "
            + "GROUP BY s.id, c.name " +
            "ORDER BY s.id DESC")
    List<ConstructorYearSummaryDto> getYearSummaryByConstructorId(Integer constructorId);

    /**
     * This method deletes a constructor with a given id.
     * @param constructorId the id of the constructor
     * @return deleted constructor
     */
    Constructor deleteConstructorById(Integer constructorId);

}