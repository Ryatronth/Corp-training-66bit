package rnn.filestorage.service.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;


@Log4j2
@Aspect
@Component
public class Aspects {
    @AfterReturning(pointcut = "execution(* rnn.filestorage.service.FileService.getFile(..))", returning = "resource")
    public void getFileAfterReturning(UrlResource resource) {
        log.info("Файл получен. {}", resource.getURL());
    }

    @AfterThrowing(pointcut = "execution(* rnn.filestorage.service.FileService.getFile(..))", throwing = "exception")
    public void getFileAfterThrowing(Exception exception) {
        log.error("Ошибка при получении файла: {}", exception.getMessage());
    }

    @AfterReturning(pointcut = "execution(* rnn.filestorage.service.FileService.uploadFile(..))", returning = "url")
    public void uploadFileAfterReturning(String url) {
        log.info("Файл создан. Url: {}", url);
    }

    @AfterThrowing(pointcut = "execution(* rnn.filestorage.service.FileService.uploadFile(..))", throwing = "exception")
    public void uploadFileAfterThrowing(Exception exception) {
        log.error("Ошибка при создании файла: {}", exception.getMessage());
    }
}
