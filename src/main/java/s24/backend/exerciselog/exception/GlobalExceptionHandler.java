package s24.backend.exerciselog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFound(UsernameNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Username was not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Handle custom exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    private static final Logger adminAccessLogger = LoggerFactory.getLogger("adminAccessLogger");

    @ExceptionHandler(ApiNotInUseException.class)
    public ResponseEntity<?> handleApiNotInUseException(ApiNotInUseException ex, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String requestUri = request.getRequestURI();
        adminAccessLogger.info("Attempt to access disabled API endpoint: {} from IP: {}. Message: {}",
                requestUri, ipAddress, ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("This API endpoint is not available");
    }

    // Handle Spring Security access denied exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
    }

    // Handle validation exceptions
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        ex.printStackTrace();

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(constraintViolation -> {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.put(propertyPath, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Handle data access exceptions (e.g., database errors)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(DataAccessException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred.");
    }

    // Handle HTTP method not supported exceptions
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP method not supported.");
    }

    // Handle encryption/decryption exceptions
    @ExceptionHandler(GeneralSecurityException.class)
    public ResponseEntity<?> handleGeneralSecurityException(GeneralSecurityException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Encryption error occurred.");
    }

    // Handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
