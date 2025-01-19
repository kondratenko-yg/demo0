package com.company.mapper;

import com.company.Message;
import com.company.dto.UpdateUserRequest;
import com.company.dto.UserBusinessDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования объектов Message в UserBusinessDto.
 */
@Mapper(componentModel = "spring")
public interface MessageToUserBusinessDtoMapper {

  /**
   * Преобразует объект Message и идентификатор города в объект UserBusinessDto.
   *
   * @param message объект сообщения из Kafka
   * @param cityId  идентификатор города
   * @return заполненный экземпляр UserBusinessDto
   */
  @Mapping(target = "fullName", source = "message.data.fullName")
  @Mapping(target = "phone", source = "message.data.phone")
  @Mapping(target = "grade", source = "message.data.grade")
  @Mapping(target = "cityId", source = "cityId")
  UserBusinessDto toUserBusinessDto(Message message, Long cityId);

  /**
   * Преобразует объект UserBusinessDto в объект UpdateUserRequest.
   *
   * @param userBusinessDto объект UserBusinessDto
   * @return объект UpdateUserRequest
   */
  UpdateUserRequest toUpdateUserRequest(UserBusinessDto userBusinessDto);
}

