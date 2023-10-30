package pl.pollub.f1data.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.pollub.f1data.Models.DTOs.RaceDto;
import pl.pollub.f1data.Models.Data.Race;

/**
 * AutoMapper for {@link Race} and {@link RaceDto}
 */
@Mapper(componentModel =  "spring", uses = {CircuitMapper.class})
public interface RaceMapper {
    /**
     * This method maps {@link Race} entity to {@link RaceDto} data transfer object
     * @param race race entity
     * @return race data transfer object
     */
    @Mapping(source = "year.id", target = "year")
    @Mapping(source = "circuit", target = "circuitDetails")
    RaceDto toDto(Race race);

}
