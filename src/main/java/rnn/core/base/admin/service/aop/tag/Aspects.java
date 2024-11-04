package rnn.core.base.admin.service.aop.tag;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import rnn.core.base.admin.dto.TagDTO;
import rnn.core.base.model.Tag;

import java.util.List;

@Log4j2
@Component("tagAspect")
@Aspect
public class Aspects {

    @AfterReturning(pointcut = "execution(* rnn.core.base.admin.service.TagService.findAll(..))", returning = "tags")
    public void afterFindPointcut(List<Tag> tags) {
        log.info("Все тэги получены. Example: {}", tags.subList(0, Math.max(0, tags.size() % 6)));
    }

    @AfterReturning(pointcut = "execution(* rnn.core.base.admin.service.TagService.findOne(..))", returning = "tag")
    public void afterFindPointcut(Tag tag) {
        log.info("Тэг получен. Тэг: {}", tag);
    }

    @AfterThrowing(pointcut = "execution(* rnn.core.base.admin.service.TagService.findOne(..)) && args(tag)", throwing = "ex")
    public void afterFindPointcut(String tag, Exception ex) {
        log.error("Ошибка при получении тэга: {}", ex.getMessage());
    }

    @AfterReturning(pointcut = "execution(* rnn.core.base.admin.service.TagService.create(..))", returning = "tag")
    public void afterCreatePointcut(Tag tag) {
        log.info("Тэг создан. Tag: {}", tag);
    }

    @AfterThrowing(pointcut = "execution(* rnn.core.base.admin.service.TagService.create(..)) && args(dto)", throwing = "ex")
    public void afterCreatePointcut(TagDTO dto, Exception ex) {
        log.error("Ошибка при создании тэга: {}. DTO: {}", ex.getMessage(), dto);
    }

    @AfterReturning(pointcut = "execution(* rnn.core.base.admin.service.TagService.update(..)) && args(tag, dto)", returning = "newtag")
    public void afterUpdatePointcut(String tag, TagDTO dto, Tag newtag) {
        log.info("Тэг изменен. Name: {} | DTO: {} | NewTag: {}", tag, dto, newtag);
    }

    @AfterThrowing(pointcut = "execution(* rnn.core.base.admin.service.TagService.update(..)) && args(tag, dto)",  throwing = "ex")
    public void afterUpdatePointcut(String tag, TagDTO dto, Exception ex) {
        log.error("Ошибка при обновлении тэга: {}. DTO: {}", ex.getMessage(), dto);
    }

    @After(value = "execution(* rnn.core.base.admin.service.TagService.delete()) && args(tag)")
    public void afterDeletePointcut(String tag) {
        log.info("Тэг удален. Tag: {}", tag);
    }
}
