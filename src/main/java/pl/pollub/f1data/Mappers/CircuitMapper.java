package pl.pollub.f1data.Mappers;

import org.mapstruct.Mapper;
import pl.pollub.f1data.Models.DTOs.CircuitDto;
import pl.pollub.f1data.Models.Data.Circuit;

@Mapper(componentModel = "spring")
public interface CircuitMapper {
    CircuitDto toDto(Circuit circuit);
}
