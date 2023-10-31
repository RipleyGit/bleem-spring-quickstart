package site.bleem.boot.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/27
 */
@Configuration
public class OnMissingBean {

    public OnMissingBean(){
        System.out.println("===================OnMissingBean==============================");
    }

//    @Bean
//    @ConditionalOnMissingBean(BeanService.class)
//    public BeanService missOnProperty(){
//        System.out.println("===================ConditionalOnMissingBean==============================");
//        return new BeanService();
//    }


}
