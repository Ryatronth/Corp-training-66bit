package rnn.core.base.admin.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import rnn.core.base.admin.dto.CourseCreationDTO;
import rnn.core.base.model.Course;

@Component
@Mapper(componentModel = "spring", uses = {UserMapper.class, TagMapper.class})
public interface CourseMapper {
    @Mapping(source = "authorName", target = "author", qualifiedByName = "authorNameToUser")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagNameToTag")
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pictureUrl", ignore = true)
    Course fromCreationDto(CourseCreationDTO courseDTO);
}
