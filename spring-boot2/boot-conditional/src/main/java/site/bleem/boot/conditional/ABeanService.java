package site.bleem.boot.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/27
 */
@ConditionalOnBean(BBeanService.class)
@Service
public class ABeanService {

    public ABeanService(){
        System.out.println("===================ABeanService==============================");
    }
}
