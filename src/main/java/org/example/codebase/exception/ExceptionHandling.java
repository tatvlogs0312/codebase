package org.example.codebase.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandling {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandling.class);

    private final String EX_INTERNAL_SERVER = "Đã có lỗi xảy ra, vui lòng thử lại sau ít phút";
    private final String EX_NOT_AUTHORIZATION = "Not Authorization";
    private final String EX_FORBIDDEN = "Not Forbidden";


    @ExceptionHandler(ApplicationException.class)
    ResponseEntity<Object> handlerApplicationException(ApplicationException e, HandlerMethod handlerMethod) {
        log.error("ExceptionHandling ApplicationException - Message: {}", e.getMessage());
        String errorCode = getIDSystem(handlerMethod);
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), e.getMessage(), errorCode), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    ResponseEntity<Object> handlerAuthorizationException(AuthorizationException e, HandlerMethod handlerMethod) {
        log.error("ExceptionHandling AuthorizationException - Message: {}", e.getMessage());
        String errorCode = getIDSystem(handlerMethod);
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), EX_NOT_AUTHORIZATION, errorCode), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    ResponseEntity<Object> handlerForbiddenException(ForbiddenException e, HandlerMethod handlerMethod) {
        log.error("ExceptionHandling ForbiddenException - Message: {}", e.getMessage());
        String errorCode = getIDSystem(handlerMethod);
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), EX_FORBIDDEN, errorCode), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handlerRuntimeException(Exception e, HandlerMethod handlerMethod) {
        String errorCode = getIDSystem(handlerMethod);
        log.error("ExceptionHandling RuntimeException - Message: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now().toString(), EX_INTERNAL_SERVER, errorCode), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getIDSystem(HandlerMethod handlerMethod) {
        RequestMapping methodAnnotation= ObjectUtils.isEmpty(handlerMethod)
                ? null
                : handlerMethod.getMethodAnnotation(RequestMapping.class);
        return "SYS" + (ObjectUtils.isEmpty(methodAnnotation) ? "NotAPI": methodAnnotation.name());
    }
}
