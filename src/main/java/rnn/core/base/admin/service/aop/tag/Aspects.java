package rnn.core.base.admin.service.aop.tag;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import rnn.core.base.admin.dto.TagDTO;
import rnn.core.base.model.Tag;

@Log4j2
@Component("tagAspect")
@Aspect
public class Aspects {
    @AfterReturning(pointcut = "rnn.core.base.admin.service.aop.tag.Pointcuts.createPointcut(dto)", argNames = "dto,tag", returning = "tag")
    public void afterCreatePointcut(TagDTO dto, Tag tag) {
        log.info("Тэг создан. DTO: {} | Tag: {}", dto, tag);
    }

    @AfterThrowing(pointcut = "rnn.core.base.admin.service.aop.tag.Pointcuts.createPointcut(dto)", argNames = "dto,ex", throwing = "ex")
    public void afterCreatePointcut(TagDTO dto, Exception ex) {
        log.error("Ошибка при создании тэга: {}. DTO: {}", ex.getMessage(), dto);
    }

    @AfterReturning(pointcut = "rnn.core.base.admin.service.aop.tag.Pointcuts.updatePointcut(tag, dto)", argNames = "tag,dto,newtag", returning = "newtag")
    public void afterUpdatePointcut(String tag, TagDTO dto, Tag newtag) {
        log.info("Тэг изменен. Name: {} | DTO: {} | NewTag: {}", dto, tag, newtag);
    }

    @AfterThrowing(pointcut = "rnn.core.base.admin.service.aop.tag.Pointcuts.updatePointcut(tag, dto)", argNames = "tag,dto,ex", throwing = "ex")
    public void afterUpdatePointcut(String tag, TagDTO dto, Exception ex) {
        log.error("Ошибка при обновлении тэга: {}. DTO: {}", ex.getMessage(), dto);
    }

    @After(value = "rnn.core.base.admin.service.aop.tag.Pointcuts.deletePointcut(tag)", argNames = "tag")
    public void afterDeletePointcut(String tag) {
        log.info("Тэг удален. Tag: {}", tag);
    }
}
