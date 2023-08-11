package site.bleem.boot.aop.conf;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import site.bleem.boot.aop.anno.OperLogAnno;
import site.bleem.boot.aop.enums.OperLogEnum;

import java.util.Objects;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/8/11
 */
@Aspect
@Component
public class OperLogAop {
    /**
     * 针对注解切面
     */
    @Pointcut("@annotation(site.bleem.boot.aop.anno.OperLogAnno)")
    public void pointcut_allow() {
    }


    /**
     *
     * @param pjp
     * @param cache
     * @return
     * @throws Throwable
     */
    @Around("pointcut_allow() && @annotation(operLogAnno)")
    public Object hazelcastCache(ProceedingJoinPoint pjp, OperLogAnno operLogAnno) throws Throwable {
        StringBuilder buf = new StringBuilder();
        buf.append(pjp.getSignature().getDeclaringTypeName());
        buf.append("." + pjp.getSignature().getName());
        for (Object obj : pjp.getArgs()) {
            if (!Objects.isNull(obj)) {
                buf.append(",");
                buf.append(obj.toString().trim());
            }
        }
        OperLogEnum operLogEnum = operLogAnno.value();
        Object value = pjp.proceed();
        System.out.printf("当前操作："+operLogEnum.name());
        return value;

    }
}
