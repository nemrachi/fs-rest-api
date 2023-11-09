package filesystem.aosd;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class Wormhole {

    @Value("${filesystem.storage-dir}")
    private String storageDir;

    @Pointcut("execution(* filesystem.services.StorageService.*(..))")
    private static void publicMethod() {
        // not used - only aspect definition
    }


    @Around("publicMethod()")
    public Object logEmaBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("CUSTOM: " + Arrays.toString(joinPoint.getArgs()));
        return joinPoint.proceed();
    }
}