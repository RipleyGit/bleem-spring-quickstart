package site.bleem.boot.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import site.bleem.boot.event.evevnt.UserChangePasswordEvent;

/**
 * 监听事件:
 * 新建一个类实现ApplicationListener接口，并且重写onApplicationEvent方法。
 * 注入到Spring容器中，交给Spring管理。如下代码。新建了一个发送短信监听器，收到事件后执行业务操作。
 *
 */
@Component
public class MessageListener implements ApplicationListener<UserChangePasswordEvent> {

    @Override
    public void onApplicationEvent(UserChangePasswordEvent event) {
        System.out.println("收到事件:" + event);
        System.out.println("开始执行业务操作给用户发送短信。用户userId为：" + event.getUserId());
    }
}