package com.company.client;

import com.company.UsersApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Feign клиент для взаимодействия с Dictionary API.
 * Интерфейс объединяет два контракта - DictionariesApi и RecordsApi.
 */
@FeignClient(name = "dictionary-api-client", url = "${dictionary.api.url}")
public interface UserClient extends UsersApi {

}
