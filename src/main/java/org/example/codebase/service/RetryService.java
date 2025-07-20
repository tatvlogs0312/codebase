package org.example.codebase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codebase.aop.Retry;
import org.example.codebase.enums.RetryEnum;
import org.example.codebase.exception.ApplicationException;
import org.example.codebase.repository.OTPRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetryService {

    private final OTPRepository otpRepository;

    @Retry(attempts = 3, typeRetry = RetryEnum.DEFAULT)
    public void retryDefault() {
        throw new ApplicationException("test retry");
    }
}
