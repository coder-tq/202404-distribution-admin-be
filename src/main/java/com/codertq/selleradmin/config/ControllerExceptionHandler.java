package com.codertq.selleradmin.config;

import com.codertq.selleradmin.domain.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * author: coder_tq
 * date: 2024/4/14
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Object> handleRuntimeExceptions(Exception ex, WebRequest request) {
        log.error("Exception: ", ex);
        Result<Object> result = Result.fail(-1, ex.getMessage(), ex);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Exception: ", ex);
        Result<Object> result = Result.fail(-1, ex.getMessage(), ex);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
