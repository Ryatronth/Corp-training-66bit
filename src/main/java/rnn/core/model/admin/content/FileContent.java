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
public class FileContent extends Content {
    private String fileUrl;

    @Override
    public String toString() {
        return "FileContent{" +
                "id=" + id +
                ", position=" + position +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
