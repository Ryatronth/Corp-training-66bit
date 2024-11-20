package rnn.core.service.admin.util.content;

import org.springframework.stereotype.Component;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.service.admin.util.content.processor.ContentProcessor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ContentManager {
    private static final Map<Content.Type, ContentProcessor<Topic>> creatorsMap = new EnumMap<>(Content.Type.class);
    private static final Map<Content.Type, ContentProcessor<Content>> updatersMap = new EnumMap<>(Content.Type.class);

    private ContentManager(List<ContentProcessor<Topic>> creators, List<ContentProcessor<Content>> updaters) {
        for (ContentProcessor<Topic> creator : creators) {
            for (Content.Type type : creator.getType()) {
                creatorsMap.put(type, creator);
            }
        }

        for (ContentProcessor<Content> updater : updaters) {
            for (Content.Type type : updater.getType()) {
                updatersMap.put(type, updater);
            }
        }
    }

    public ContentProcessor<Topic> getCreator(Content.Type type) {
        return creatorsMap.get(type);
    }

    public ContentProcessor<Content> getUpdater(Content.Type type) {
        return updatersMap.get(type);
    }
}
