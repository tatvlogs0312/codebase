package org.example.codebase.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.codebase.common.JwtUtils;
import org.example.codebase.exception.AuthorizationException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SecuredAspect {

    @Before(value = "@annotation(org.example.codebase.aop.Secured)")
    public void before(JoinPoint j) {
        String username = JwtUtils.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new AuthorizationException();
        }

        //Get role for use api
        MethodSignature signature = (MethodSignature) j.getSignature();
        Method method = signature.getMethod();
        var s = method.getAnnotation(Secured.class);
        String[] roleEnums = s.roles();
    }
}
