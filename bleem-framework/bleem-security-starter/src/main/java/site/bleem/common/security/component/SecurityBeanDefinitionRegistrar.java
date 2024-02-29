package site.bleem.common.security.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

public class SecurityBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger log = LoggerFactory.getLogger(SecurityBeanDefinitionRegistrar.class);

    public SecurityBeanDefinitionRegistrar() {
    }

    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        if (registry.isBeanNameInUse("resourceServerConfigurerAdapter")) {
            log.warn("本地存在资源服务器配置，覆盖默认配置:resourceServerConfigurerAdapter");
        } else {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(ResourceServerConfigurerAdapter.class);
            registry.registerBeanDefinition("resourceServerConfigurerAdapter", beanDefinition);
        }
    }
}
