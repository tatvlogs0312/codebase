package org.example.codebase.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    public static String getUsername() {
        try {
            HttpServletRequest request;
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            if (Objects.nonNull(servletRequestAttributes)) {
                request = servletRequestAttributes.getRequest();
                String authorization = request.getHeader("Authorization");
                String token = StringUtils.isNotBlank(authorization) ? authorization.replace("Bearer ", "") : "";
                if (StringUtils.isNotBlank(token)) {
                    DecodedJWT dJWT = JWT.decode(token);
                    return dJWT.getClaim("preferred_username").asString();
                }
            }
            return "";
        } catch (Exception e) {
            log.info("JwtUtils getUsername - Exception: {}", e.getMessage());
        }
        return "";
    }

    public static String getToken() {
        try {
            HttpServletRequest request;
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            if (Objects.nonNull(servletRequestAttributes)) {
                request = servletRequestAttributes.getRequest();
                String authorization = request.getHeader("Authorization");
                return StringUtils.isNotBlank(authorization) ? authorization.replace("Bearer ", "") : "";
            }
            return "";
        } catch (Exception e) {
            log.info("JwtUtils getToken - Exception: {}", e.getMessage());
        }
        return "";
    }
}
