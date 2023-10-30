package pl.pollub.f1data.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.pollub.f1data.Models.DTOs.DriverDto;
import pl.pollub.f1data.Models.Data.Driver;

/**
 * AutoMapper interface for mapping {@link DriverDto} to {@link Driver} entity
 */
@Mapper(componentModel =  "spring")
public interface AutoMapper {

    /**
     * Instance of AutoMapper
     */
    AutoMapper INSTANCE = Mappers.getMapper(AutoMapper.class);

    /**
     * Method for mapping DriverDto to Driver entity
     * @param driver driver data
     * @return mapped driver entity
     */
    Driver mapToDriver(DriverDto driver);

}
