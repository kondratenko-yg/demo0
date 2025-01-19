package com.company.service.impl;

import com.company.client.DictionaryClient;
import com.company.dto.AttributeDto;
import com.company.dto.ExampleConditionDto;
import com.company.dto.QueryDto;
import com.company.dto.RecordDataDto;
import com.company.dto.RecordDtoResponse;
import com.company.dto.RecordsDtoResponse;
import com.company.exception.CityServiceException;
import com.company.service.CityService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

  private final DictionaryClient dictionaryClient;

  /**
   * Получает идентификатор города по его названию.
   * Если город не найден, создает новую запись и возвращает его идентификатор.
   *
   * @param cityName название города
   * @return идентификатор города
   * @throws CityServiceException если произошла ошибка при взаимодействии с API
   */
  public Long getCityIdByName(String cityName) {
    try {
      // Условие для поиска записи с атрибутом "name" равным cityName
      ExampleConditionDto condition = new ExampleConditionDto();
      condition.setName("name");
      condition.setAttribute(cityName);

      // Создание объекта запроса
      QueryDto queryDto = new QueryDto();
      queryDto.setByExample(Collections.singletonList(condition));

      // Поиск записи в справочнике
      ResponseEntity<RecordsDtoResponse> searchResponse =
          dictionaryClient.searchRecords("City", queryDto);

      if (searchResponse.getBody() != null &&
          !searchResponse.getBody().getData().isEmpty()) {
        // Если запись найдена, возвращаем её идентификатор
        return searchResponse.getBody().getData().get(0).getId();
      } else {
        // Если запись не найдена, создаем новую
        RecordDataDto newRecord = new RecordDataDto();
        newRecord.setAttributes(Collections.singletonList(
            new AttributeDto().name("name").value(cityName)
        ));

        ResponseEntity<RecordDtoResponse> createResponse =
            dictionaryClient.createRecord("City", newRecord);

        if (createResponse.getBody() != null && createResponse.getBody().getData() != null) {
          // Возвращаем идентификатор созданной записи
          return createResponse.getBody().getData().getId();
        } else {
          throw new CityServiceException("City creation failed, response is empty.");
        }
      }

    } catch (HttpClientErrorException e) {
      throw new CityServiceException("Ошибка при вызове API справочника: " + e.getMessage(), e);
    }
  }
}
