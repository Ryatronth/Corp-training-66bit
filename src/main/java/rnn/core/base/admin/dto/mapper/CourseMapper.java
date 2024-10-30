package rnn.core.base.admin.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import rnn.core.base.admin.dto.CourseCreationDTO;
import rnn.core.base.admin.dto.CourseDTO;
import rnn.core.base.model.Course;

@Component
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CourseMapper {
    @Mapping(source = "authorName", target = "author", qualifiedByName = "authorNameToUser")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pictureUrl", ignore = true)
    Course fromCreationDto(CourseCreationDTO courseDTO);

    @Mapping(source = "authorName", target = "author", qualifiedByName = "authorNameToUser")
    Course fromDto(CourseDTO courseDTO);

    @Mapping(source = "authorName", target = "author", qualifiedByName = "authorNameToUser")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Course updateFromDto(CourseDTO dto, @MappingTarget Course course);
}
