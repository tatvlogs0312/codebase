package org.example.codebase.aop.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.codebase.aop.DuplicateRequest;
import org.example.codebase.exception.ApplicationException;
import org.example.codebase.redis.RedisService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DuplicateRequestAspect {

    private final RedisService redisService;

    @Around(value = "@annotation(preventDuplicateRequest)")
    public Object handleDuplicateRequest(ProceedingJoinPoint joinPoint, DuplicateRequest preventDuplicateRequest) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // Sinh ra key theo URL + method + sessionId hoặc IP hoặc header gì đó để phân biệt
        String key = generateKey(request, preventDuplicateRequest.key());

        if (StringUtils.isBlank(key)) {
            return joinPoint.proceed();
        }

        if (redisService.hasKey(key)) {
            log.warn("Duplicate request detected with key: {}", key);
            throw new ApplicationException("Yêu cầu đang được xử lý");
        }

        redisService.setValue(key, LocalDateTime.now().toString(), preventDuplicateRequest.keyExpire());

        try {
            return joinPoint.proceed();
        } finally {
            redisService.delete(key);
        }
    }

    private String generateKey(HttpServletRequest request, String prefix) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String userIdentifier = request.getHeader("Authorization");

        return prefix + ":" + method + ":" + uri + ":" + userIdentifier;
    }

}
