package site.bleem.common.security.component;

import cn.hutool.core.util.StrUtil;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.security.access.AccessDeniedException;
import site.bleem.common.security.annotation.Inner;

@Aspect
public class SecurityInnerAspect implements Ordered {
    private static final Logger log = LoggerFactory.getLogger(SecurityInnerAspect.class);
    private final HttpServletRequest request;

    @Around("@annotation(inner)")
    public Object around(ProceedingJoinPoint point, Inner inner) throws Throwable {
        try {
            String header = this.request.getHeader("from");
            if (inner.value() && !StrUtil.equals("Y", header)) {
                log.warn("访问接口 {} 没有权限", point.getSignature().getName());
                throw new AccessDeniedException("Access is denied");
            } else {
                return point.proceed();
            }
        } catch (Throwable var4) {
            throw var4;
        }
    }

    public int getOrder() {
        return -2147483647;
    }

    public SecurityInnerAspect(HttpServletRequest request) {
        this.request = request;
    }
}
