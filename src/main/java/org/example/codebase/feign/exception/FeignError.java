package org.example.codebase.feign.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.example.codebase.constants.Constants;
import org.example.codebase.exception.ApplicationException;
import org.example.codebase.exception.ErrorResponse;

import java.io.InputStream;

public class FeignError implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        try {
            if (response.body() != null) {
                try (InputStream bodyIs = response.body().asInputStream()) {
                    ErrorResponse errorResponse = objectMapper.readValue(bodyIs, ErrorResponse.class);
                    throw new ApplicationException(errorResponse.getMessage());
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(Constants.INTERNAL_SERVER_ERROR);
        }

        throw new ApplicationException(Constants.INTERNAL_SERVER_ERROR);
    }
}
