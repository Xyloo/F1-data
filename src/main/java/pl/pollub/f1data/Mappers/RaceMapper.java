package pl.pollub.f1data.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.pollub.f1data.Models.DTOs.RaceDto;
import pl.pollub.f1data.Models.Data.Race;

@Mapper(componentModel =  "spring", uses = {CircuitMapper.class})
public interface RaceMapper {
    @Mapping(source = "year.id", target = "year")
    @Mapping(source = "circuit", target = "circuitDetails")
    RaceDto toDto(Race race);

}
