package rnn.core.base.admin.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import rnn.core.base.admin.service.TagService;
import rnn.core.base.model.Tag;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TagMapper {
    private final TagService tagService;

    @Named("tagNameToTag")
    public List<Tag> tagNameToTag(List<String> tags) {
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tags) {
            Tag tag = tagService.findOne(tagName);
            tagList.add(tag);
        }
        return tagList;
    }
}
