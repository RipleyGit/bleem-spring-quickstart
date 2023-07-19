package site.bleem.boot.hazelcast.controller;

import site.bleem.boot.hazelcast.feign.ClientSentryFeignPoxy;
import site.bleem.boot.hazelcast.model.User;
import site.bleem.boot.hazelcast.service.UserService;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Watter on 2018-03-22
 */
@RestController
public class TestController {


    @Autowired
    private ClientSentryFeignPoxy clientSentryFeignPoxy;
    @Autowired
    private UserService userService;


    @GetMapping("/hello")
    public void hello() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            Future<User> f1 = clientSentryFeignPoxy.findAn("1");
            Future<User> f2 = clientSentryFeignPoxy.findAn("1");
            Future<User> f3 = clientSentryFeignPoxy.findAn("3");
            Thread.sleep(1000);
            Future<User> f4 = clientSentryFeignPoxy.findAn("4");
            Future<User> f5 = clientSentryFeignPoxy.findAn("5");

            User u1 = f1.get();
            User u2 = f2.get();
            User u3 = f3.get();

            User u4 = f4.get();
            User u5 = f5.get();
            System.out.println(u1.getName());
            System.out.println(u2.getName());
            System.out.println(u3.getName());
            System.out.println(u4.getName());
            System.out.println(u5.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            context.close();
        }
    }

    @GetMapping("/haha")
    public void haha() {
        try {
            for (int i = 1; i < 6; i++) {
                userService.userby(String.valueOf(i));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/world")
    public void world() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            userService.user1();
            userService.user2();
            userService.user3();
            userService.user4();
            userService.user5();
            userService.user6();
            userService.user7();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            context.close();
        }
    }
}
