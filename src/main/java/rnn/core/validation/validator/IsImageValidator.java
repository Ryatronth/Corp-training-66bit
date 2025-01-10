package rnn.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.validation.annotations.IsImage;

@Log4j2
public class IsImageValidator implements ConstraintValidator<IsImage, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image");
    }
}
