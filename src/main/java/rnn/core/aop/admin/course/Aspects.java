package rnn.core.aop.admin.course;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import rnn.core.model.admin.dto.CourseWithImageDTO;
import rnn.core.model.admin.Course;

@Log4j2
@Component("courseAspect")
@Aspect
public class Aspects {
    @AfterReturning(pointcut = "execution(* rnn.core.service.admin.CourseService.create(..))", returning = "course")
    public void afterSaveCoursePointcut(Course course) {
        log.info("Курс создан. Course: {}", course);
    }

    @AfterThrowing(pointcut = "execution(* rnn.core.service.admin.CourseService.create(..)) && args(courseDTO)", throwing = "ex")
    public void afterSaveCoursePointcut(CourseWithImageDTO courseDTO, Exception ex) {
        log.error("Ошибка при создании курса: {}. DTO: {}", ex.getMessage(), courseDTO);
    }
}
