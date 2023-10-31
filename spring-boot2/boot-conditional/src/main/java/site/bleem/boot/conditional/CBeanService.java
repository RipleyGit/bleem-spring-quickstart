package site.bleem.boot.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/27
 */

public class CBeanService {

    public CBeanService(){
        System.out.println("===================CBeanService==============================");
    }
}
