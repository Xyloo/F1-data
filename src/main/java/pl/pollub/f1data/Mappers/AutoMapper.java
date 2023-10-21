package pl.pollub.f1data.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.pollub.f1data.Models.DTOs.DriverDto;
import pl.pollub.f1data.Models.Data.Driver;

@Mapper(componentModel =  "spring")
public interface AutoMapper {

    AutoMapper INSTANCE = Mappers.getMapper(AutoMapper.class);
    Driver mapToDriver(DriverDto driver);

}
