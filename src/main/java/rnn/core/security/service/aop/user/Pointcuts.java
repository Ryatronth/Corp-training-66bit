package rnn.core.security.service.aop.user;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut(value = "execution(* rnn.core.security.service.UserService.checkUserExisted(..))")
    public void checkPointcut() {
    }

    @Pointcut(value = "execution(* rnn.core.security.service.UserService.getUser(..))")
    public void getPointcut() {
    }
}
