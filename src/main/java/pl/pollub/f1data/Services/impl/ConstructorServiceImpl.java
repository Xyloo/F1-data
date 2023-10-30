package pl.pollub.f1data.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.f1data.Models.DTOs.ConstructorResultsDto;
import pl.pollub.f1data.Models.DTOs.ConstructorYearSummaryDto;
import pl.pollub.f1data.Models.Data.Constructor;
import pl.pollub.f1data.Repositories.F1Database.ConstructorRepository;
import pl.pollub.f1data.Services.ConstructorService;

import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for handling requests related to constructors.
 */
@Service
public class ConstructorServiceImpl implements ConstructorService {

    @Autowired
    private ConstructorRepository constructorRepository;

    @Override
    public Optional<Constructor> getConstructorById(Integer constructorId) {
        return constructorRepository.findById(constructorId);
    }

    @Override
    public List<Constructor> getAllConstructors() {
        return constructorRepository.findAll();
    }

    @Override
    public List<Constructor> getAllConstructorsByNationality(String nationality) {
        return constructorRepository.findConstructorByNationality(nationality);
    }

    @Override
    public List<ConstructorResultsDto> getConstructorResultsByConstructorIdAndYear(Integer constructorId, int year) {
        return constructorRepository.getConstructorResultsByConstructorIdAndYear(constructorId, year);
    }

    @Override
    public List<ConstructorYearSummaryDto> getConstructorStandingsByConstructorId(Integer constructorId) {
        return constructorRepository.getYearSummaryByConstructorId(constructorId);
    }

    @Override
    public Optional<Constructor> deleteConstructorById(Integer constructorId) {
        Optional<Constructor> constructor = constructorRepository.findById(constructorId);
        if (constructor.isPresent()) {
            constructorRepository.deleteConstructorById(constructorId);
        }
        return constructor;
    }

}
