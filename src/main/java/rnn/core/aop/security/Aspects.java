package rnn.core.aop.security;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
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

    @AfterReturning(pointcut = "rnn.core.aop.security.Pointcuts.getPointcut()", argNames = "user", returning = "user")
    public void getPointcut(User user) {
        log.info("Информация о пользователе получена. User: {}", user);
    }

    @AfterThrowing(pointcut = "rnn.core.aop.security.Pointcuts.getPointcut()", argNames = "ex", throwing = "ex")
    public void getPointcut(Exception ex) {
        log.info("Ошибка при получении пользователя: {}", ex.getMessage());
    }
}
