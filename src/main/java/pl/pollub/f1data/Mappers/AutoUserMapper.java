package pl.pollub.f1data.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.pollub.f1data.Models.DTOs.CreateUserDTO;
import pl.pollub.f1data.Models.User;

@Mapper
public interface AutoUserMapper {

    AutoUserMapper MAPPER = Mappers.getMapper(AutoUserMapper.class);
    CreateUserDTO mapToUserDto(User user);
    User mapToUser(CreateUserDTO createUserDTO);
}
