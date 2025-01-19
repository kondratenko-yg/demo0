package com.company.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.company.client.DictionaryClient;
import com.company.dto.AttributeDto;
import com.company.dto.QueryDto;
import com.company.dto.RecordDataDto;
import com.company.dto.RecordDto;
import com.company.dto.RecordDtoResponse;
import com.company.dto.RecordsDtoResponse;
import com.company.exception.CityServiceException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class CityServiceTest {

  private static final String DICTIONARY_NAME = "City";

  @Mock
  private DictionaryClient dictionaryClient;

  @InjectMocks
  private CityService cityService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetCityIdByNameCityExists() {
    // Mock response when city exists
    RecordDto existingRecord = new RecordDto();
    existingRecord.setId(1L);
    existingRecord.setAttributes(Collections.singletonList(new AttributeDto().name("name").value("ExampleCity")));

    RecordsDtoResponse response = new RecordsDtoResponse();
    response.setData(Collections.singletonList(existingRecord));

    when(dictionaryClient.searchRecords(eq(DICTIONARY_NAME), any(QueryDto.class)))
        .thenReturn(ResponseEntity.ok(response));

    Long cityId = cityService.getCityIdByName("ExampleCity");
    assertEquals(1L, cityId);
  }

  @Test
  void testGetCityIdByNameCityNotExists() {
    // Mock response when city does not exist
    RecordsDtoResponse emptyResponse = new RecordsDtoResponse();
    emptyResponse.setData(Collections.emptyList());

    when(dictionaryClient.searchRecords(eq(DICTIONARY_NAME), any(QueryDto.class)))
        .thenReturn(ResponseEntity.ok(emptyResponse));

    RecordDtoResponse createdRecordResponse = new RecordDtoResponse();
    RecordDto newRecord = new RecordDto();
    newRecord.setId(2L);
    createdRecordResponse.setData(newRecord);

    when(dictionaryClient.createRecord(eq(DICTIONARY_NAME), any(RecordDataDto.class)))
        .thenReturn(ResponseEntity.ok(createdRecordResponse));

    Long cityId = cityService.getCityIdByName("NewCity");
    assertEquals(2L, cityId);
  }


  @Test
  void testGetCityIdByNameThrowsException() {
    when(dictionaryClient.searchRecords(eq(DICTIONARY_NAME), any(QueryDto.class)))
        .thenThrow(new CityServiceException("Service not available"));

    assertThrows(CityServiceException.class, () -> cityService.getCityIdByName("FaultyCity"));
  }
}
