package com.company.mapper;

import com.company.Message;
import com.company.dto.UpdateUserRequest;
import com.company.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер для преобразования из Message и UserDto в собственные DTO.
 */
@Mapper
public interface MessageToUserMapper {

  MessageToUserMapper INSTANCE = Mappers.getMapper(MessageToUserMapper.class);

  /**
   * Преобразует Message из Kafka и идентификатор города в UserDto.
   *
   * @param message информация из Kafka
   * @param cityId  идентификатор города
   * @return экземпляр UserDto
   */
  @Mapping(source = "message.data.fullName", target = "fullName")
  @Mapping(source = "message.data.phone", target = "phone")
  @Mapping(source = "cityId", target = "city")
  @Mapping(source = "message.data.grade", target = "grade")
  @Mapping(source = "message.data.timestamp", target = "timestamp")
  UserDto toUserDto(Message message, Long cityId);

  /**
   * Преобразует UserDto в UpdateUserRequest.
   *
   * @param userDto объект UserDto
   * @return экземпляр UpdateUserRequest
   */
  @Mapping(source = "fullName", target = "fio")
  @Mapping(source = "city", target = "cityId", qualifiedByName = "longToInteger")
  UpdateUserRequest toUpdateUserRequest(UserDto userDto);

  /**
   * Методы для преобразования типов.
   */
  default Integer longToInteger(Long value) {
    return value != null ? value.intValue() : null;
  }

  default Integer longToInteger(long value) {
    return (int) value;
  }
}
