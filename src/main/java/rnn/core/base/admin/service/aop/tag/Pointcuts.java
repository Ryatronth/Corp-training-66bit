package rnn.core.base.admin.service.aop.tag;

import org.aspectj.lang.annotation.Pointcut;
import rnn.core.base.admin.dto.TagDTO;

public class Pointcuts {
    @Pointcut(value = "execution(* rnn.core.base.admin.service.TagService.create(dto))", argNames = "dto")
    public void createPointcut(TagDTO dto) {}

    @Pointcut(value = "execution(* rnn.core.base.admin.service.TagService.create(tag, dto))", argNames = "tag,dto")
    public void updatePointcut(String tag, TagDTO dto) {}

    @Pointcut(value = "execution(* rnn.core.base.admin.service.TagService.delete(tag))", argNames = "tag")
    public void deletePointcut(String tag) {}
}
