package s24.backend.exerciselog.util;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

// Processing BindingResult for ResponseEntity
public class ValidationUtil {
    public static ResponseEntity<Map<String, String>> handleValidationErrors(BindingResult result) {
        if(result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    FieldError::getField, FieldError::getDefaultMessage
                ));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        return null;
    }
}
