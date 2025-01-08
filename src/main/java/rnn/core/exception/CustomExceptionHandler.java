package rnn.core.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> handleAlreadyExistException(DataIntegrityViolationException e) {
        String message;

        String errorMessage = e.getMessage();

        if (errorMessage.contains("unique_course")) {
            message = "Курс с таким названием уже существует.";
        } else if (errorMessage.contains("unique_title_course")) {
            message = "Модуль с таким именем уже существует.";
        }else if (errorMessage.contains("unique_title_module")) {
            message = "Тема с таким названием уже существует.";
        } else if (errorMessage.contains("unique_position_module")) {
            message = "Тема с такой позицией уже существует.";
        } else if (errorMessage.contains("unique_group_name_course")) {
            message = "Группа с таким названием уже существует.";
        } else {
            throw e;
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionDTO
                        .builder()
                        .message(message)
                        .build()
                );
    }
}
