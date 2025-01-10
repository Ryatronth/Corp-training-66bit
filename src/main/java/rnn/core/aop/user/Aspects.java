package rnn.core.aop.user;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import rnn.core.model.user.dto.AnswerDTO;

@Log4j2
@Component("userCourseAspect")
@Aspect
public class Aspects {
    @Around("execution(* rnn.core.service.user.UserContentService.answer(..))")
    public Object afterSaveCoursePointcut(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        log.info("Дан ответ на задание.");
        log.info("Аргументы: {}", args);

        AnswerDTO result = (AnswerDTO) joinPoint.proceed();
        log.info("UserContent: {}", result.content());
        log.info("UserTopic: {}", result.topic());
        log.info("UseModule: {}", result.module());
        log.info("UserCourse: {}", result.course());

        return result;
    }
}
