package rnn.core.model.admin.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.dto.contentImpl.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DetailedContentDTO.class, name = "DETAILED_ANSWER"),
        @JsonSubTypes.Type(value = AnswerContentDTO.class, names = {"SINGLE_ANSWER", "MULTI_ANSWER"}),
        @JsonSubTypes.Type(value = FreeformContentDTO.class, name = "FREEFORM_ANSWER"),
        @JsonSubTypes.Type(value = FileContentDTO.class, names = {"PICTURE", "VIDEO"}),
        @JsonSubTypes.Type(value = TextContentDTO.class, names = {"TEXT"})
})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@EqualsAndHashCode
public class ContentDTO {
    protected int position;

    protected Content.Type type;

    protected String title;

    protected String description;
}
