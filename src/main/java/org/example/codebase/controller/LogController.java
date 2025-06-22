package org.example.codebase.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.example.codebase.model.MailDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    @GetMapping("/get")
    public ResponseEntity<Object> logGet() {
        log.info("1");
        log.info("2");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<Object> logPost(@RequestBody MailDTO mailDTO) {
        log.info("1");
        log.info("2");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
