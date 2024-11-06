package rnn.core.base.admin.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import rnn.core.base.admin.dto.CourseCreationDTO;
import rnn.core.base.model.Course;
import rnn.core.base.model.converter.TagConverter;

@Component
@Mapper(componentModel = "spring", uses = {UserMapper.class, TagConverter.class})
public interface CourseMapper {
    @Mapping(source = "authorName", target = "author", qualifiedByName = "authorNameToUser")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "stringToTags")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "pictureUrl", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "modules", ignore = true)
    @Mapping(target = "id", ignore = true)
    Course fromCreationDto(CourseCreationDTO courseDTO);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "stringToTags")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "pictureUrl", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "modules", ignore = true)
    @Mapping(target = "id", ignore = true)
    Course updateFromCreationDto(@MappingTarget Course course, CourseCreationDTO courseDTO);
}
