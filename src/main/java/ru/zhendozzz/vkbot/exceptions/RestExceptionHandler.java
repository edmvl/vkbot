package ru.zhendozzz.vkbot.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleIdNotFound(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ApiError apiError = new ApiError("error", ex.toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
