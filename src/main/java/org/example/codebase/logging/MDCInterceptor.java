package org.example.codebase.logging;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class MDCInterceptor extends RequestBodyAdviceAdapter implements HandlerInterceptor, ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest httpServletRequest;

    private Object requestBody;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uuid = UUID.randomUUID().toString();
        MDC.put("requestId", uuid);

        String url = request.getRequestURL().toString();
        String query = request.getQueryString();
        if (query != null) {
            url += "?" + query;
        }
        MDC.put("requestUrl", url);
        MDC.put("httpMethod", request.getMethod());
        MDC.put("requestHeader", request.getHeader("Authorization"));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.clear();
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        try {
            // Log the request body directly into MDC here.
            String json = objectMapper.writeValueAsString(body);
            MDC.put("requestBody", json);
        } catch (Exception e) {
            log.warn("Error converting request body to JSON for MDC: {}", e.getMessage());
        }
        return body; // Return the body so it can continue to be processed
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            // Log the response body here.
            String json = objectMapper.writeValueAsString(body);
            MDC.put("responseBody", json); // You can put the response body into MDC as well
        } catch (Exception e) {
            log.warn("Error converting response body to JSON for MDC: {}", e.getMessage());
        }
        return body;
    }

    public String getRequestBody(HttpServletRequest request, Object requestBody) {
        String body = null;
        if (request.getHeader("Content-Type") != null && !Objects.requireNonNull(request.getHeader("Content-Type")).contains("multipart/form-data")) {
            body = new String(convertToBytes(requestBody), StandardCharsets.UTF_8);
        }
        return body;
    }

    public byte[] convertToBytes(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            log.info("convert Exception");
        }
        return new byte[0];
    }
}
