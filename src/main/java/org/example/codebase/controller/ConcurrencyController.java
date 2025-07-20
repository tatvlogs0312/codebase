package org.example.codebase.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codebase.aop.DuplicateRequest;
import org.example.codebase.redis.ConcurrencyRequestCheck;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concurrency")
@RequiredArgsConstructor
@Slf4j
public class ConcurrencyController {

    private final ConcurrencyRequestCheck requestCheck;

    @GetMapping(value = "/run", name = "RUN")
    public ResponseEntity<Object> run() {
        requestCheck.run("RUN", () -> {
            log.info("RUN");
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/suppler", name = "SUPPLER")
    public ResponseEntity<Object> suppler() {
        String sup = requestCheck.supply("SUPPLER", () -> {
            return "SUPPLER";
        });
        log.info(sup);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DuplicateRequest(key = "annotation", keyExpire = 60 * 10)
    @GetMapping(value = "/annotation", name = "ANNOTATION")
    public ResponseEntity<Object> checkDupUseAnnotation() throws InterruptedException {
        log.info("checkDupUseAnnotation");
        Thread.sleep(1000 * 60 * 2);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
