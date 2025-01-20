package com.company.client;

import com.company.DictionaryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "dictionary-api-client", url = "${app.dictionary-url}")
public interface DictionaryClient extends DictionaryApi {

}
