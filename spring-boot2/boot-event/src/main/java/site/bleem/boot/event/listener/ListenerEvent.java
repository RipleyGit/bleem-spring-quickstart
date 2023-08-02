package site.bleem.boot.event.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import site.bleem.boot.event.evevnt.UserChangePasswordEvent;

/**
 * 监听事件:
 * 使用 @EventListener 注解标注处理事件的方法，此时 Spring 将创建一个 ApplicationListener bean 对象，使用给定的方法处理事件。源码如下。参数可以给指定的事件。
 * 这里巧妙的用到了@AliasFor的能力，放到了@EventListener身上 注意：一般建议都需要指定此值，否则默认可以处理所有类型的事件，范围太广了。
 * 新建一个事件监听器，注入到Spring容器中，交给Spring管理。在指定方法上添加@EventListener参数为监听的事件。方法为业务代码。
 * 使用 @EventListener 注解的好处是一个类可以写很多监听器，定向监听不同的事件，或者同一个事件。
 */
@Component
public class ListenerEvent {

    @EventListener({ UserChangePasswordEvent.class })
    public void LogListener(UserChangePasswordEvent event) {
        System.out.println("收到事件:" + event);
        System.out.println("开始执行业务操作生成关键日志。用户userId为：" + event.getUserId());
    }


    /**
     * @TransactionalEventListener来定义一个监听器，
     * 他与@EventListener不同的就是@EventListener标记一个方法作为监听器，他默认是同步执行，
     * 如果发布事件的方法处于事务中，那么事务会在监听器方法执行完毕之后才提交。
     * 事件发布之后就由监听器去处理，而不要影响原有的事务，也就是说希望事务及时提交。
     * 我们就可以使用该注解来标识。注意此注解需要spring-tx的依赖。
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,value = { UserChangePasswordEvent.class })
    public void messageListener(UserChangePasswordEvent event) {
        System.out.println("收到事件:" + event);
        System.out.println("开始执行业务操作给用户发送短信。用户userId为：" + event.getUserId());
    }
}