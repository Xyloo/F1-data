package pl.pollub.f1data.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.pollub.f1data.Models.DTOs.ResultDto;
import pl.pollub.f1data.Models.Data.Result;

/**
 * Mapper for {@link Result} and {@link ResultDto}
 */
@Mapper(componentModel =  "spring")
public interface ResultMapper {

    /**
     * This method maps {@link Result} to {@link ResultDto}
     * @param result result
     * @return {@link ResultDto}
     */
    @Mapping(target = "driverName", expression = "java(result.getDriver().getForename() + \" \" + result.getDriver().getSurname())")
    @Mapping(target = "constructorName", expression = "java(result.getConstructor().getName())")
    @Mapping(target = "statusDetails", expression = "java(result.getStatus().getStatus())")
    ResultDto toDto(Result result);
}
