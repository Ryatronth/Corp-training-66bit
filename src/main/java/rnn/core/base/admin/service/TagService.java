package rnn.core.base.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.base.admin.dto.TagDTO;
import rnn.core.base.exception.AlreadyExistException;
import rnn.core.base.model.Tag;
import rnn.core.base.model.repository.TagRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    public Tag create(TagDTO tagDTO) {
        if (tagRepository.existsById(tagDTO.name())) {
            throw new AlreadyExistException("Тэг с именем \"%s\" уже существует".formatted(tagDTO.name()));
        }
        Tag tag = Tag.builder().name(tagDTO.name()).color(tagDTO.color()).build();
        return tagRepository.save(tag);
    }

    public Tag update(String tag, TagDTO tagDTO) {
        Tag existTag = tagRepository.findById(tag).orElseThrow(() -> new IllegalArgumentException("Тэг с названием \"%s\" не существует".formatted(tag)));
        existTag.setName(tagDTO.name());
        existTag.setColor(tagDTO.color());
        return tagRepository.save(existTag);
    }

    public void delete(String tag) {
        tagRepository.deleteById(tag);
    }

    public Tag findOne(String tag) {
        return tagRepository.findById(tag).orElseThrow(() -> new IllegalArgumentException("Тэг с названием \"%s\" не существует".formatted(tag)));
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}
