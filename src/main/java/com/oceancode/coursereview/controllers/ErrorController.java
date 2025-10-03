package com.oceancode.coursereview.controllers;

import com.oceancode.coursereview.domain.dtos.ErrorDto;
import com.oceancode.coursereview.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(ReviewNotAllowedException.class)
    public ResponseEntity<ErrorDto> handleReviewNotAllowedException(ReviewNotAllowedException ex) {
        log.error("Caught ReviewNotAllowedException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorDto> handleReviewNotFoundException(ReviewNotFoundException ex) {
        log.error("Caught ReviewNotFoundException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCourseNotFoundException(CourseNotFoundException ex) {
        log.error("Caught CourseNotFoundException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("Caught UserNotFoundException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Caught MethodArgumentNotValidException", ex);

        String errorMessage = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    // Handle our base application exception
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDto> handleBaseException(BaseException ex) {
        log.error("Caught BaseException", ex);
        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Catch-all for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Caught unexpected exception", ex);
        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
