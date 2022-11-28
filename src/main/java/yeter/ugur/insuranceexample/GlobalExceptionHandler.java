package yeter.ugur.insuranceexample;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import yeter.ugur.insuranceexample.api.creation.PolicyDateException;
import yeter.ugur.insuranceexample.api.PolicyIsNotFoundException;

import java.time.Instant;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Global exception handling, provides uniform interface and control on returned error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {PolicyDateException.class})
    public ResponseEntity<ErrorResponse> handlePolicyDateException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(value = {PolicyIsNotFoundException.class})
    public ResponseEntity<ErrorResponse> handlePolicyNotFound(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, HttpStatus notFound) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(Instant.now().toEpochMilli())
                .build(), notFound);
    }
}
