package org.example.codebase.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandling {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandling.class);

    private final String EX_INTERNAL_SERVER = "Đã có lỗi xảy ra, vui lòng thử lại sau ít phút";
    private final String EX_NOT_AUTHORIZATION = "Not Authorization";
    private final String EX_FORBIDDEN = "Not Forbidden";


    @ExceptionHandler(ApplicationException.class)
    ResponseEntity<Object> handlerApplicationException(ApplicationException e) {
        log.error("ExceptionHandling ApplicationException - Message: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    ResponseEntity<Object> handlerAuthorizationException(AuthorizationException e) {
        log.error("ExceptionHandling AuthorizationException - Message: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), EX_NOT_AUTHORIZATION), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    ResponseEntity<Object> handlerForbiddenException(ForbiddenException e) {
        log.error("ExceptionHandling ForbiddenException - Message: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), EX_FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handlerRuntimeException(Exception e) {
        log.error("ExceptionHandling RuntimeException - Message: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), EX_INTERNAL_SERVER), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
