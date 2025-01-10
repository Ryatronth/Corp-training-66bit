package rnn.core.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import rnn.core.validation.validator.FileNotEmptyValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = FileNotEmptyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FileNotEmpty {
    String message() default "Файл не должен быть пустым.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
