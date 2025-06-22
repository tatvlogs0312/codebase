package org.example.codebase.feign;

import org.example.codebase.feign.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "myClient", url = "https://api.example.com", configuration = FeignConfiguration.class)
public interface Client {

    @GetMapping("/api/data")
    String getData();
}
