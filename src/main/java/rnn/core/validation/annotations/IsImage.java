package rnn.core.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import rnn.core.validation.validator.IsImageValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = IsImageValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsImage {
    String message() default "Файл не является изображением.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
