package org.example.codebase.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.example.codebase.exception.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
@Slf4j
public class ExceptionController {

    @Operation(summary = "API test mã lỗi")
    @GetMapping(value = "/", name = "EX")
    public ResponseEntity<Object> logGet() {
        throw new ApplicationException("TEST");
    }
}
