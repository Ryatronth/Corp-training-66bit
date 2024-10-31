package rnn.core.base.admin.service.aop.course;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.base.admin.dto.CourseCreationDTO;
import rnn.core.base.model.Course;

@Log4j2
@Component("courseAspect")
@Aspect
public class Aspects {
    @AfterReturning(pointcut = "rnn.core.base.admin.service.aop.course.Pointcuts.saveCoursePointcut(dto, file)", argNames = "dto,file,course", returning = "course")
    public void afterSaveCoursePointcut(CourseCreationDTO dto, MultipartFile file, Course course) {
        log.info("Курс создан. DTO: {} | File: {} | Course: {}", dto, file, course);
    }

    @AfterThrowing(pointcut = "rnn.core.base.admin.service.aop.course.Pointcuts.saveCoursePointcut(dto, file)", argNames = "dto,file,ex", throwing = "ex")
    public void afterSaveCoursePointcut(CourseCreationDTO dto, MultipartFile file, Exception ex) {
        log.error("Ошибка при создании курса: {}. DTO: {} | File: {}", ex.getMessage(), dto, file);
    }
}
