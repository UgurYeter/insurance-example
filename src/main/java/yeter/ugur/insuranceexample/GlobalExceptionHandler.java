package yeter.ugur.insuranceexample;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import yeter.ugur.insuranceexample.api.PolicyCreationException;
import yeter.ugur.insuranceexample.api.PolicyModificationException;

import java.time.Instant;

/**
 * Global exception handling, provides uniform interface and control on returned error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {PolicyCreationException.class, PolicyModificationException.class})
    public ResponseEntity<ErrorResponse> handlePolicyCreationExceptions(Exception exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(Instant.now().toEpochMilli())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
