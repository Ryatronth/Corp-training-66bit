package rnn.core.model.admin.content;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import rnn.core.model.admin.Content;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Entity
@ToString
public class TextContent extends Content {

}
