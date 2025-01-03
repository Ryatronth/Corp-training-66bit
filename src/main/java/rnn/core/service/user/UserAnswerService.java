package rnn.core.service.user;

import org.springframework.stereotype.Service;
import rnn.core.model.admin.content.DetailedContent;
import rnn.core.model.admin.content.FreeformContent;
import rnn.core.model.user.UserAnswer;
import rnn.core.model.user.UserContent;

import java.util.List;

@Service
public class UserAnswerService {
    public UserAnswer create(UserContent userContent, List<String> answer) {
        if (userContent.getContent() instanceof FreeformContent freeformContent) {
            if (freeformContent instanceof DetailedContent) {
                return UserAnswer.builder().answer(answer.toString()).content(userContent).build();
            } else {
                return UserAnswer.builder().answer(answer.getFirst()).content(userContent).build();
            }
        }
        return null;
    }
}
