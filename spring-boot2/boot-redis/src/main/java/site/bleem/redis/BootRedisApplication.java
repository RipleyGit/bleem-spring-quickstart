package site.bleem.redis;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BootRedisApplication {

    public static void main(String[] args) {
        System.out.println(""+Runtime.getRuntime().availableProcessors());
        SpringApplication.run(BootRedisApplication.class, args);
    }
}