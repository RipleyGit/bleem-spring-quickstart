package site.bleem.boot.web.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import site.bleem.boot.web.cache.LocalCacheManager;
import site.bleem.boot.web.common.AccessLimit;
import site.bleem.boot.web.util.StringUtils;

/**
 * @author yubs
 * @desc todo
 * @date 2023/12/1
 */
@Slf4j
@Aspect
@Configuration
public class AccessLimitAop {

    @Pointcut("@annotation(site.bleem.boot.web.common.AccessLimit)")
    public void limitAccess() {}

    @Before("limitAccess() && @annotation(accessLimit)")
    public void limitAccessAop(JoinPoint joinPoin, AccessLimit accessLimit) throws Throwable {
        int maxCount = accessLimit.maxCount();
        String message = accessLimit.message();
        Object[] args = joinPoin.getArgs();
        String string = JSON.toJSONString(args);
        string = string.trim();
        String md5Hash = StringUtils.getMD5Hash(string);
        LocalCacheManager instance = LocalCacheManager.getInstance();
        Integer integer = instance.get(md5Hash);
        if (integer == null){
            integer = 0;
        }
        if (integer > maxCount){
            throw new RuntimeException(message);
        }else {
            integer++;
            instance.put(md5Hash,integer);
        }
    }

}
