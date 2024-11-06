package rnn.core.base.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import rnn.core.base.model.Tag;

import java.util.List;

@Component
@Converter(autoApply = true)
public class TagConverter implements AttributeConverter<List<Tag>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Tag> tags) {
        try {
            return mapper.writeValueAsString(tags);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Named("stringToTags")
    @Override
    public List<Tag> convertToEntityAttribute(String s) {
        try {
            return mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, Tag.class));
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при парсинге тэгов. Json: %s".formatted(s));
        }
    }
}
