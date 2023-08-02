package site.bleem.boot.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import site.bleem.boot.event.evevnt.UserChangePasswordEvent;

/**
 * 发布一个事件：
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SpirngEventApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationEventPublisher appEventPublisher;

    /**
     * 使用ApplicationContext进行发布，由于ApplicationContext 已经继承了 ApplicationEventPublisher ，因此可以直接使用发布事件。
     */
    @Test
    public void applicationContextPublishEvent() {
        applicationContext.publishEvent(new UserChangePasswordEvent("1111111"));
    }

    /**
     * 直接注入我们的ApplicationEventPublisher，使用@Autowired注入一下。
     */
    @Test
    public void appEventPublisherPublishEvent() {
        appEventPublisher.publishEvent(new UserChangePasswordEvent("222222"));
    }

}