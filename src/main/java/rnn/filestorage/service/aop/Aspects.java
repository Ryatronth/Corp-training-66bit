package rnn.filestorage.service.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Log4j2
@Aspect
public class Aspects {
    @Value("${server.api_v1}")
    private String apiV1;

    @AfterReturning(pointcut = "rnn.filestorage.service.aop.Pointcuts.getFilePointcut(uuid)", argNames = "uuid")
    public void getFileAfterReturning(UUID uuid) {
        log.info("Файл получен. Url: {}/courses/{}", apiV1, uuid);
    }

    @AfterThrowing(pointcut = "rnn.filestorage.service.aop.Pointcuts.getFilePointcut(uuid)", throwing = "exception", argNames = "uuid,exception")
    public void getFileAfterThrowing(UUID uuid, Exception exception) {
        log.error(exception.getMessage());
    }

    @AfterReturning(pointcut = "rnn.filestorage.service.aop.Pointcuts.uploadFilePointcut()", returning = "url")
    public void uploadFileAfterReturning(String url) {
        log.info("Файл создан. Url: {}", url);
    }

    @AfterThrowing(pointcut = "rnn.filestorage.service.aop.Pointcuts.uploadFilePointcut()", throwing = "exception")
    public void uploadFileAfterThrowing(Exception exception) {
        log.error(exception.getMessage());
    }
}
