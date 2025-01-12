package rnn.core.aop.security;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut(value = "execution(* rnn.core.service.security.UserService.checkUserExisted(..))")
    public void checkPointcut() {
    }
}
