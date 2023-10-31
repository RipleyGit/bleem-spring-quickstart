package site.bleem.boot.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/27
 */
@Configuration
@ConditionalOnProperty(prefix = "filter",name = "loginFilter",havingValue = "true")
public class OnProperty {
    public OnProperty(){
        System.out.println("===================OnProperty==============================");
    }

    @Bean
    @ConditionalOnProperty(name="filter.loginFilter")
    BeanService beanService() {
        return new BeanService();
    }


}
