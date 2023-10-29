package pl.pollub.f1data.Repositories.F1Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.pollub.f1data.Models.DTOs.ConstructorResultsDto;
import pl.pollub.f1data.Models.DTOs.ConstructorYearSummaryDto;
import pl.pollub.f1data.Models.Data.Constructor;

import java.util.List;

public interface ConstructorRepository extends JpaRepository<Constructor, Integer> {
    List<Constructor> findConstructorByNationality(String nationality);

    @Query("SELECT new pl.pollub.f1data.Models.DTOs.ConstructorResultsDto(cs.id, cs.constructor.id, cs.race.id, cs.race.circuit.name, cs.points, cs.position) " +
            "FROM Constructorstanding cs " +
            "JOIN Constructor c ON cs.constructor.id = c.id " +
            "WHERE cs.constructor.id = ?1 and cs.race.year.id = ?2")
    List<ConstructorResultsDto> getConstructorResultsByConstructorIdAndYear(Integer constructorId, Integer year);

    @Query("SELECT new pl.pollub.f1data.Models.DTOs.ConstructorYearSummaryDto(s.id, c.name, SUM(cs.points)) " +
            "FROM Season s join Race r on r.year.id = s.id " +
            "join Constructorstanding cs on r.id = cs.race.id " +
            "join Constructor c on c.id = cs.constructor.id " +
            "WHERE c.id = ?1 "
            + "GROUP BY s.id, c.name " +
            "ORDER BY s.id DESC")
    List<ConstructorYearSummaryDto> getYearSummaryByConstructorId(Integer constructorId);

    Constructor deleteConstructorById(Integer constructorId);

}