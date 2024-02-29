package site.bleem.boot.hazelcast.feign;

import site.bleem.boot.hazelcast.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;

@Component
public class ClientSentryFeignPoxy {

    @Autowired
    ClientSentryFeign clientSentryFeign;


    @HystrixCollapser(batchMethod = "findAnAll",
            collapserProperties = {@HystrixProperty(name ="timerDelayInMilliseconds",value = "1000")})
    public Future<User> findAn(String id) {
        User user = clientSentryFeign.singleUser(id);
        return new AsyncResult<>(user);
    }

    @HystrixCommand
    public List<User> findAnAll(List<String> ids) {
        System.out.println("finaAnAll request:---------" + ids + "Thread.currentThread().getName():-------" + Thread.currentThread().getName());
        List<User> list = clientSentryFeign.batchUser(ids);
        return list;
    }

    public User getById(String id) {
        System.out.println("request:---------" + id + "Thread.currentThread().getName():-------" + Thread.currentThread().getName());
        return clientSentryFeign.singleUser(id);
    }
}
