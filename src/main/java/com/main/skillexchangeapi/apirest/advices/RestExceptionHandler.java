package com.main.skillexchangeapi.apirest.advices;

import com.main.skillexchangeapi.app.error.ResponseError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class RestExceptionHandler {
    private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class.getName());

    @ResponseBody
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseError> handleResponseStatusException(ResponseStatusException ex) {
        ResponseError responseError = ResponseError.builder()
                .status(ex.getStatusCode())
                .error(ex.getReason())
                .message(ex.getMessage())
                .build();

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(responseError, headers, ex.getStatusCode());
    }
}
