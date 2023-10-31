package site.bleem.boot.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/27
 */
//@ConditionalOnBean(BeanService.class)
@ConditionalOnMissingBean(BeanService.class)
//@ConditionalOnProperty(name="filter.loginFilter.server")
@Service
public class BBeanService {

    public BBeanService(){
        System.out.println("===================BBeanService==============================");
    }
}
