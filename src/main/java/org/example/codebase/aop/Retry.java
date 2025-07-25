package org.example.codebase.aop;

import org.example.codebase.enums.RetryEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int attempts() default 3;
    RetryEnum typeRetry() default RetryEnum.DEFAULT;
}
