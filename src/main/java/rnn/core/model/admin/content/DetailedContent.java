package rnn.core.model.admin.content;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import rnn.core.model.admin.Answer;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DetailedContent extends FreeformContent {
    private int countAttempts;

    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Answer> answers;

    @Override
    public String toString() {
        return "DetailedContent{" +
                "id=" + id +
                ", position=" + position +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", countAttempts=" + countAttempts +
                ", answers=" + answers +
                '}';
    }
}
