package com.company.client;

import com.company.UsersApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Клиент для взаимодействия с бизнес-сервисом пользователей.
 */
@FeignClient(name = "usersClient", url = "${external.users-service.url}")
public interface UsersClient extends UsersApi {
  // Наследуется от UsersApi, дополнительные методы не требуются
}
