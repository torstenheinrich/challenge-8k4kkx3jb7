package com.redcare.challenge.controller;

import com.redcare.challenge.service.RepositoryServiceException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RepositoryControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        var body = createProblemDetail(e, HttpStatus.BAD_REQUEST, e.toString(), null, null, request);

        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RepositoryServiceException.class)
    ResponseEntity<Object> handleRepositoryServiceException(RepositoryServiceException e, WebRequest request) {
        var body = createProblemDetail(e, HttpStatus.BAD_GATEWAY, e.toString(), null, null, request);

        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }
}
