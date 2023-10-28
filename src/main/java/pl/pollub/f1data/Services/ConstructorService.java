package pl.pollub.f1data.Services;

import pl.pollub.f1data.Models.DTOs.ConstructorResultsDto;
import pl.pollub.f1data.Models.DTOs.ConstructorYearSummaryDto;
import pl.pollub.f1data.Models.Data.Constructor;
import java.util.List;
import java.util.Optional;

public interface ConstructorService {

    Optional<Constructor> getConstructorById(Integer constructorId);
    List<Constructor> getAllConstructors();
    List<Constructor> getAllConstructorsByNationality(String nationality);
    List<ConstructorResultsDto> getConstructorResultsByConstructorIdAndYear(Integer constructorId, int year);
    List<ConstructorYearSummaryDto> getConstructorStandingsByConstructorId(Integer constructorId);
    Optional<Constructor> deleteConstructorById(Integer constructorId);

}
