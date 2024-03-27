package com.market.transactionguard.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class GlobalException {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        HttpStatus statusCode = (HttpStatus) ex.getStatusCode();

        if (statusCode == HttpStatus.UNPROCESSABLE_ENTITY) {
            // Handle Unprocessable Entity response (status code 422) here
            String errorMessage = "The server could not process the request. Please check your input.";
            return ResponseEntity.status(statusCode).body(errorMessage);
        }

        if (statusCode.is4xxClientError()) {
            String errorMessage = "Client error: " + statusCode.getReasonPhrase();
            return ResponseEntity.status(statusCode).body(errorMessage);
        }
        if (statusCode.is5xxServerError()) {
            String errorMessage = "Server error: " + statusCode.getReasonPhrase();
            return ResponseEntity.status(statusCode).body(errorMessage);
        }

        // Handle other WebClientResponseException scenarios or rethrow
        return ResponseEntity.status(statusCode).body(ex.getResponseBodyAsString());
    }


}
