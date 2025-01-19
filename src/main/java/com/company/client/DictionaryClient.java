package com.company.client;

import com.company.DictionaryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Клиент для взаимодействия с сервисом справочных данных.
 */
@FeignClient(name = "dictionaryClient", url = "${external.dictionary.url}")
public interface DictionaryClient extends DictionaryApi {
  // Наследуется от DictionaryApi, дополнительные методы не требуются
}
