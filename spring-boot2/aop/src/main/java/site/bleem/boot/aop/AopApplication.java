package site.bleem.boot.aop;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AopApplication implements CommandLineRunner {

    public static void main(String[] args) {

        new SpringApplicationBuilder(AopApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}