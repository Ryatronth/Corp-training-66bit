package rnn.core.service.admin.util.contentCreator;

import org.springframework.stereotype.Component;
import rnn.core.model.admin.Content;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ContentFactory {
    private static final Map<Content.Type, ContentCreator> creatorsMap = new EnumMap<>(Content.Type.class);

    private ContentFactory(List<ContentCreator> creators) {
        for (ContentCreator creator : creators) {
            for (Content.Type type : creator.getType()) {
                creatorsMap.put(type, creator);
            }
        }
    }

    public ContentCreator getCreator(Content.Type type) {
        return creatorsMap.get(type);
    }
}
