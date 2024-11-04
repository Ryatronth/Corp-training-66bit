package rnn.core.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ExceptionDTO> handleAlreadyExistException(AlreadyExistException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionDTO
                        .builder()
                        .message(e.getMessage())
                        .error(e.getStackTrace()[0])
                        .build()
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionDTO
                        .builder()
                        .message(e.getMessage())
                        .error(e.getStackTrace()[0])
                        .build()
                );
    }
}
