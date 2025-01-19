package com.company.mapper;

import com.company.Message;
import com.company.UserData;
import com.company.dto.UpdateUserRequest;
import com.company.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования объектов Message в UserDto.
 */
@Mapper(componentModel = "spring")
public interface MessageToUserDtoMapper {

  /**
   * Преобразует объект Message и идентификатор города в объект UserDto.
   *
   * @param data объект сообщения из Kafka
   * @param cityId  идентификатор города
   * @return заполненный экземпляр UserDto
   */
  @Mapping(target = "fullName", source = "data.fullName")
  @Mapping(target = "phone", source = "data.phone")
  @Mapping(target = "grade", source = "data.grade")
  @Mapping(target = "cityId", source = "cityId")
  UserDto toUserDto(UserData data, Long cityId);

  /**
   * Преобразует объект UserDto в объект UpdateUserRequest.
   *
   * @param userDto объект UserDto
   * @return объект UpdateUserRequest
   */
  UpdateUserRequest toUpdateUserRequest(UserDto userDto);
}

