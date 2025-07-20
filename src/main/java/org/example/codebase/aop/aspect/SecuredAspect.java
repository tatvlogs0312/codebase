package org.example.codebase.aop.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.codebase.aop.Secured;
import org.example.codebase.common.JwtUtils;
import org.example.codebase.exception.AuthorizationException;
import org.example.codebase.exception.ForbiddenException;
import org.example.codebase.redis.RedisService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SecuredAspect {

    private final RedisService redisService;

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
        if (roleEnums.length > 0) {
            String roleOfApi = roleEnums[0];
            //Verify role
            if (StringUtils.isNotBlank(roleOfApi)) {
                String roleOfUser = redisService.getValue(username + "_role");
                if (!Objects.equals(roleOfApi, roleOfUser)) {
                    throw new ForbiddenException("Không có quyền truy cập");
                }
            }
        }
    }
}
