package site.bleem.common.security.component;

import cn.hutool.core.util.ReUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import site.bleem.common.security.annotation.Inner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Configuration
@ConfigurationProperties(
    prefix = "security.oauth2.ignore"
)
public class PermitAllUrlProperties implements InitializingBean, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(PermitAllUrlProperties.class);
    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");
    private ApplicationContext applicationContext;
    private List<String> urls = new ArrayList();

    public PermitAllUrlProperties() {
    }

    public void afterPropertiesSet() {
        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping)this.applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach((info) -> {
            HandlerMethod handlerMethod = (HandlerMethod)map.get(info);
            Inner method = (Inner) AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
            Optional.ofNullable(method).ifPresent((inner) -> {
                info.getPatternsCondition().getPatterns().forEach((url) -> {
                    this.urls.add(ReUtil.replaceAll(url, PATTERN, "*"));
                });
            });
            Inner controller = (Inner)AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);
            Optional.ofNullable(controller).ifPresent((inner) -> {
                info.getPatternsCondition().getPatterns().forEach((url) -> {
                    this.urls.add(ReUtil.replaceAll(url, PATTERN, "*"));
                });
            });
        });
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

    public List<String> getUrls() {
        return this.urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
