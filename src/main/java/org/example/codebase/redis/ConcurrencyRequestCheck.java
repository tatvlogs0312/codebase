package org.example.codebase.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.codebase.exception.ApplicationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConcurrencyRequestCheck {

    private final RedisService redisService;

    public void run(String key, Runnable runnable) {
        if (StringUtils.isNotBlank(key)) {
            runnable.run();
            return;
        }
        if (redisService.hasKey(key)) {
            throw new ApplicationException("Yêu cầu đang được xử lý");
        }
        redisService.setValue(key, LocalDateTime.now().toString(), 30L);
        try {
            runnable.run();
        } finally {
            redisService.delete(key);
        }
    }

    public <R> R supply(String key, Supplier<R> supplier) {
        if (StringUtils.isNotBlank(key)) {
            return supplier.get();
        }
        if (redisService.hasKey(key)) {
            throw new ApplicationException("Yêu cầu đang được xử lý");
        }
        redisService.setValue(key, LocalDateTime.now().toString(), 30L);
        try {
            return supplier.get();
        } finally {
            redisService.delete(key);
        }
    }
}
