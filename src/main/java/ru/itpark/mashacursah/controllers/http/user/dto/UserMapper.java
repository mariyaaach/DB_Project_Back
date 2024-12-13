package ru.itpark.mashacursah.controllers.http.user.dto;

import org.mapstruct.*;
import ru.itpark.mashacursah.entity.user.User;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User toEntity(GetUserDto getUserDto);

    @Mapping(target = "role", source = "role.role")
    GetUserDto toDto(User user);
    List<GetUserDto> toDto(List<User> user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(GetUserDto getUserDto, @MappingTarget User user);

    User toEntity(UpdateUserDto updatedUser);
}
