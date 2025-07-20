package org.example.codebase.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codebase.service.RetryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retry")
@RequiredArgsConstructor
@Slf4j
public class RetryController {

    private final RetryService retryService;

    @GetMapping(value = "/default", name = "RE")
    public ResponseEntity<Object> logGet() {
        retryService.retryDefault();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
