package com.company.service;

import com.company.client.DictionaryClient;
import com.company.dto.*;
import com.company.exception.CityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Сервис для работы с городами.
 */
@Service
@RequiredArgsConstructor
public class CityService {

  public static final String CITY = "City";
  private final DictionaryClient dictionaryClient;

  /**
   * Получает идентификатор города по его названию.
   *
   * @param cityName название города
   * @return идентификатор города
   */
  public Long getCityId(String cityName) {
    // Создание объекта запроса для поиска
    QueryDto queryDto = new QueryDto();
    ExampleConditionDto exampleCondition = new ExampleConditionDto();
    exampleCondition.setName("name");
    exampleCondition.setAttribute(cityName);
    queryDto.setByExample(Collections.singletonList(exampleCondition));

    // Поиск существующих записей
    ResponseEntity<RecordsDtoResponse> response = dictionaryClient.searchRecords(CITY, queryDto);
    if (response != null && response.getBody() != null && response.getBody().getData() != null) {
      return response.getBody().getData().get(0).getId();
    } else {
      // Если записи нет, создаем новую
      RecordDataDto recordDataDto = new RecordDataDto();
      recordDataDto.setAttributes(Collections.singletonList(new AttributeDto().name("name").value(cityName)));

      ResponseEntity<RecordDtoResponse> createResponse = dictionaryClient.createRecord("City", recordDataDto);
      if (createResponse != null && createResponse.getBody() != null) {
        return createResponse.getBody().getData().getId();
      } else {
        throw new CityNotFoundException("Не удалось создать запись для города: " + cityName);
      }
    }
  }
}
