package s24.backend.exerciselog.exception;

public class ApiNotInUseException extends RuntimeException {
    public ApiNotInUseException(String message) {
        super(message);
    }
}
