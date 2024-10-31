package rnn.core.base.admin.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import rnn.core.base.model.Tag;
import rnn.core.base.model.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TagMapper {
    private final TagRepository tagRepository;

    @Named("tagNameToTag")
    public List<Tag> tagNameToTag(List<String> tags) {
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tags) {
            Tag tag = tagRepository.findById(tagName).orElseThrow(() -> new IllegalArgumentException("Тэг с именем \"%s\" не найден"));
            tagList.add(tag);
        }
        return tagList;
    }
}
