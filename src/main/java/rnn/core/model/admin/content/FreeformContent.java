package rnn.core.model.admin.content;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import rnn.core.model.admin.Content;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FreeformContent extends Content {
    private int score;

    @Override
    public String toString() {
        return "FreeformContent{" +
                "id=" + id +
                ", position=" + position +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", score=" + score +
                '}';
    }
}
