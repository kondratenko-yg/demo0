package com.company.service.impl;

import com.company.UserData;
import com.company.mapper.MessageToUserDtoMapper;
import com.company.service.CityService;
import com.company.service.ProcessService;
import com.company.service.UserService;
import com.company.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

  private final UserService userService;
  private final MessageToUserDtoMapper mapper;
  private final CityService cityService;

  @Override
  public void process(UserData data) {
    Long cityId = cityService.getCityIdByName(data.getCity());

    // Преобразование Message в UserDto
    UserDto userDto = mapper.toUserDto(data, cityId);

    // Обновление пользователя
    userService.updateUser(userDto);
  }
}
