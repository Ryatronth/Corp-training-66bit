package rnn.core.base.admin.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import rnn.core.base.admin.dto.TopicDTO;
import rnn.core.base.model.Topic;

@Component
@Mapper(componentModel = "spring")
public interface TopicMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "module", ignore = true)
//    @Mapping(target = "contents", ignore = true)
    @Mapping(target = "id", ignore = true)
    Topic fromDTO(TopicDTO dto);

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "module", ignore = true)
//    @Mapping(target = "contents", ignore = true)
    @Mapping(target = "id", ignore = true)
    Topic updateFromDTO(@MappingTarget Topic topic, TopicDTO dto);
}
