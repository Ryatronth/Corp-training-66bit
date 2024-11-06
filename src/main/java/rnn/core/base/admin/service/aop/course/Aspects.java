package rnn.core.base.admin.service.aop.course;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import rnn.core.base.admin.dto.CourseDTO;
import rnn.core.base.model.Course;

@Log4j2
@Component("courseAspect")
@Aspect
public class Aspects {
    @AfterReturning(pointcut = "execution(* rnn.core.base.admin.service.CourseService.create(..))", returning = "course")
    public void afterSaveCoursePointcut(Course course) {
        log.info("Курс создан. Course: {}", course);
    }

    @AfterThrowing(pointcut = "execution(* rnn.core.base.admin.service.CourseService.create(..)) && args(courseDTO)", throwing = "ex")
    public void afterSaveCoursePointcut(CourseDTO courseDTO, Exception ex) {
        log.error("Ошибка при создании курса: {}. DTO: {}", ex.getMessage(), courseDTO);
    }
}
