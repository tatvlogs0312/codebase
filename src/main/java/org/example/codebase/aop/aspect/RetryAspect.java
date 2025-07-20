package org.example.codebase.aop.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.codebase.aop.Retry;
import org.example.codebase.enums.RetryEnum;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class RetryAspect {

    @Around(value = "@annotation(retry)")
    public Object retryAdvice(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        if (Objects.equals(retry.typeRetry(), RetryEnum.DEFAULT)) {
            int attempts = retry.attempts();
            int tryCount = 0;
            Throwable lastException = null;

            while (tryCount < attempts) {
                try {
                    return joinPoint.proceed();
                } catch (Throwable ex) {
                    lastException = ex;
                    tryCount++;
                    System.out.println("Retry " + tryCount + "/" + attempts + " failed: " + ex.getMessage());
                }
            }

            System.out.println("All retry attempts failed.");
            throw Objects.requireNonNull(lastException);
        } else if (Objects.equals(retry.typeRetry(), RetryEnum.OTP)) {
            return joinPoint.proceed();
        } else {
            return joinPoint.proceed();
        }
    }
}
