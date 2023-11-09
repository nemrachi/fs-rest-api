package filesystem.aosd;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class Wormhole {

    @Pointcut("execution(public * *(..))")
    private static void publicMethod() {
        // not used - only aspect definition
    }

    @Pointcut("within(@filesystem.aosd.AOSD *)")
    private static void logEma() {
    }

    @Around(value = "publicMethod() && logEma()")
    public Object logEmaBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("CUSTOM: ", Arrays.toString(joinPoint.getArgs()));
        return joinPoint.proceed();
    }
}