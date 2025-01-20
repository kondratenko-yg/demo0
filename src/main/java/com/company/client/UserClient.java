package com.company.client;

import com.company.UsersApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-api-client", url = "${app.user-url}")
public interface UserClient extends UsersApi {

}
