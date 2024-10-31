package rnn.core.base.admin.service.aop.course;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.base.admin.dto.CourseCreationDTO;

public class Pointcuts {
    @Pointcut(value = "execution(* rnn.core.base.admin.service.CourseService.createAndSave(dto, file))", argNames = "dto,file")
    public void saveCoursePointcut(CourseCreationDTO dto, MultipartFile file) {}
}
