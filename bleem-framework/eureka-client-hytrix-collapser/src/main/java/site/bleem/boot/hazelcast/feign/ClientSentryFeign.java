package site.bleem.boot.hazelcast.feign;

import site.bleem.boot.hazelcast.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "client-sentry")
public interface ClientSentryFeign {
    @RequestMapping("/timeOut")
    String timeOut(@RequestParam int mills);

    @RequestMapping("/testCache")
    String testCache(@RequestParam String str);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    List<User> batchUser(@RequestParam(value = "ids")List<String> ids);

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    User singleUser(@PathVariable("id") String id);
}
