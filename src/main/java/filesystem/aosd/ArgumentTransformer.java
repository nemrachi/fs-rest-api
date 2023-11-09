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
public class ArgumentTransformer {

    @Value("${filesystem.storage-dir}")
    private String storageDir;

    @Pointcut(value = "execution(* filesystem.services.*.*(String)) && args(file)", argNames = "file")
    private static void filePointcut(String file) {}

    @Pointcut(value = "execution(* filesystem.services.*.getPattern(String,..)) && args(file,..)", argNames = "file")
    private static void patternPointcut(String file) {}

    @Pointcut("@annotation(PatternPointcut)")
    public void annotationPattern() {}

    @Pointcut(value = "execution(* filesystem.services.*.*(String, String)) && args(src, dest)", argNames = "src,dest")
    private static void srcDestPointcut(String src, String dest) {}

    @Around(value = "filePointcut(file) || patternPointcut(file)", argNames = "pjp,file")
    public Object buildPath(ProceedingJoinPoint pjp, String file) throws Throwable {
        String joinedPath = String.join("/", storageDir, file);
        log.info("Method argument transformation by aspect [method: " + pjp.getSignature().toShortString() + ", old: " + file + ", new: " + joinedPath + "]");
        if (pjp.getArgs().length == 2) {
            return pjp.proceed(new Object[]{joinedPath,  Arrays.stream(pjp.getArgs()).toList().get(1)});
        }
        return pjp.proceed(new Object[]{joinedPath});
    }

    @Around(value = "srcDestPointcut(src,dest) && !annotationPattern()", argNames = "pjp,src,dest")
    public Object buildPath(ProceedingJoinPoint pjp, String src, String dest) throws Throwable {
        String joinedSrc = String.join("/", storageDir, src);
        String joinedDest = String.join("/", storageDir, dest);
        log.info("Method argument transformation by aspect [method: " + pjp.getSignature().toShortString() + ", old: [" + src + ", " + dest + "], new: [" + joinedSrc + ", " + joinedDest + "]");
        return pjp.proceed(new Object[]{joinedSrc, joinedDest});
    }
}