package site.bleem.boot.hazelcast.service;

import site.bleem.boot.hazelcast.feign.ClientSentryFeignPoxy;
import site.bleem.boot.hazelcast.model.User;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class UserService {

    @Autowired
    private ClientSentryFeignPoxy clientSentryFeignPoxy;

    public void userby(String id) throws ExecutionException, InterruptedException {
        User user = clientSentryFeignPoxy.getById(id);
        System.out.println(System.currentTimeMillis()+"user"+ JSON.toJSONString(user));
    }

    public void user1() throws ExecutionException, InterruptedException {
        Future<User> future = clientSentryFeignPoxy.findAn("1");
        User user = future.get();
        System.out.println("user1"+ JSON.toJSONString(user));
    }

    public void user2()throws ExecutionException, InterruptedException {
        Future<User> future = clientSentryFeignPoxy.findAn("2");
        User user = future.get();
        System.out.println("user2"+ JSON.toJSONString(user));
    }

    public void user3()throws ExecutionException, InterruptedException {
        Future<User> future = clientSentryFeignPoxy.findAn("3");
        User user = future.get();
        System.out.println("user3"+ JSON.toJSONString(user));
    }

    public void user4()throws ExecutionException, InterruptedException {
        Future<User> future = clientSentryFeignPoxy.findAn("4");
        User user = future.get();
        System.out.println("user4"+ JSON.toJSONString(user));
    }

    public void user5()throws ExecutionException, InterruptedException {
        Future<User> future = clientSentryFeignPoxy.findAn("5");
        User user = future.get();
        System.out.println("user5"+ JSON.toJSONString(user));
    }

    public void user6()throws ExecutionException, InterruptedException {
        Future<User> future1 = clientSentryFeignPoxy.findAn("5");
        Future<User> future4 = clientSentryFeignPoxy.findAn("3");
        User user1 = future1.get();
        User user4 = future4.get();
        System.out.println("user6"+ JSON.toJSONString(user1)+JSON.toJSONString(user4));
    }

    public void user7()throws ExecutionException, InterruptedException {
        Thread.sleep(1000);
        Future<User> future1 = clientSentryFeignPoxy.findAn("4");
        Future<User> future4 = clientSentryFeignPoxy.findAn("5");
        User user1 = future1.get();
        User user4 = future4.get();
        System.out.println("user7"+ JSON.toJSONString(user1)+JSON.toJSONString(user4));
    }
}
