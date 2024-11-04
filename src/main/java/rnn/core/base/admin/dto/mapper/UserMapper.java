package rnn.core.base.admin.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import rnn.core.security.model.User;
import rnn.core.security.service.UserService;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final UserService userService;

    @Named("authorNameToUser")
    public User authorNameToUser(String authorName) {
        return userService.findOne(authorName);
    }
}
