package site.bleem.boot.event.evevnt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 定义一个事件:
 * UserChangePasswordEvent方法。定义我们监听器需要的业务参数，监听器需要那些参数，我们这里就定义那些参数。
 */
@Getter
@Setter
public class UserChangePasswordEvent extends ApplicationEvent {
    private String userId;

    public UserChangePasswordEvent(String userId) {
        super(new Object());
        this.userId = userId;
    }
}