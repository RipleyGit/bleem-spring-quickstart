package site.bleem.boot.aop.conf;


import site.bleem.boot.aop.anno.BleemAnno;
import com.hazelcast.core.Hazelcast;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Objects;

@Aspect
@Component
public class BleemTestAop {

    /**
     * 针对注解切面
     */
    @Pointcut("@annotation(site.bleem.boot.aop.anno.BleemAnno)")
    public void pointcut_allow() {
    }

    /**
     *
     * @param pjp
     * @param cache
     * @return
     * @throws Throwable
     */
    @Around("pointcut_allow() && @annotation(cache)")
    public Object hazelcastCache(ProceedingJoinPoint pjp, BleemAnno cache) throws Throwable {
        StringBuilder buf = new StringBuilder();
        buf.append(pjp.getSignature().getDeclaringTypeName());
        buf.append("." + pjp.getSignature().getName());
        for (Object obj : pjp.getArgs()) {
            if (!Objects.isNull(obj)) {
                buf.append(",");
                buf.append(obj.toString().trim());
            }
        }
        String key = encrypt2MD5(buf.toString().trim());
        Object value = Hazelcast.getHazelcastInstanceByName(CacheConfig.HAZELCAST_INSTANCE).getMap(cache.value()).get(key);
        if (Objects.isNull(value)) {
            value = pjp.proceed();
            Hazelcast.getHazelcastInstanceByName(CacheConfig.HAZELCAST_INSTANCE).getMap(cache.value()).put(key, value);
            System.out.println("存入缓存："+key);
        }else {
            System.out.println("缓存中取出："+value.toString());
        }
        return value;

    }

    public static String encrypt2MD5(String str){
        String hexStr ="";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(str.getBytes("utf-8"));
            hexStr = ByteUtils.toHexString(digest);
        } catch (Exception e) {
            return str.trim();
        }
        return hexStr;
    }

}
