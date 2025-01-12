package rnn.core.aop.security;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import rnn.core.model.security.User;

@Log4j2
@Component("userAspect")
@Aspect
public class Aspects {
    @AfterReturning(pointcut = "rnn.core.aop.security.Pointcuts.checkPointcut()", argNames = "user", returning = "user")
    public void checkPointcut(User user) {
        log.info("Контекст создан. User: {}", user);
    }
}
