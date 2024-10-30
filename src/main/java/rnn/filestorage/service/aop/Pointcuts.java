package rnn.filestorage.service.aop;

import org.aspectj.lang.annotation.Pointcut;

import java.util.UUID;

public class Pointcuts {
    @Pointcut(value = "execution(* rnn.filestorage.service.FileService.getCourseFile(uuid))", argNames = "uuid")
    public void getFilePointcut(UUID uuid) {
    }

    @Pointcut(value = "execution(* rnn.filestorage.service.FileService.uploadCourseFile(..))")
    public void uploadFilePointcut() {
    }
}
