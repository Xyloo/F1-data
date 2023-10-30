package pl.pollub.f1data.Mappers;

import org.mapstruct.Mapper;
import pl.pollub.f1data.Models.DTOs.CircuitDto;
import pl.pollub.f1data.Models.Data.Circuit;

/**
 * AutoMapper interface for mapping {@link CircuitDto} to {@link Circuit} entity
 */
@Mapper(componentModel = "spring")
public interface CircuitMapper {
    /**
     * Method for mapping {@link Circuit} entity to {@link CircuitDto}
     * @param circuit circuit data
     * @return mapped {@link CircuitDto}
     */
    CircuitDto toDto(Circuit circuit);
}
