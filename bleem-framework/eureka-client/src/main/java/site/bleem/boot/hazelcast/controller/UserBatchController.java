package site.bleem.boot.hazelcast.controller;

import site.bleem.boot.hazelcast.model.Customer;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by lisen on 2017/12/27.
 */
@RestController
public class UserBatchController {

    static HashMap<String,Customer> map = new HashMap<>();

    static {
        map.put("1",new Customer(1l, "小明"));
        map.put("2",new Customer(2l, "小红"));
        map.put("3",new Customer(3l, "小张"));
        map.put("4",new Customer(4l, "小王"));
        map.put("5",new Customer(5l, "小李"));
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<Customer> batchUser(@RequestParam(value = "ids")List<String> ids) {
        List li = new ArrayList();
        System.out.println("ids===:" + ids);
        for (String id:ids) {
            Customer customer = map.get(id);
            li.add(customer);
        }
        return li;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public Customer singleUser(@PathVariable("id") String id) {
        return map.get(id);
    }

}
