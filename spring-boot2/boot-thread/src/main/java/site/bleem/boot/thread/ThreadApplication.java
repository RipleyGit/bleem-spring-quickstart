package site.bleem.boot.thread;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ThreadApplication implements CommandLineRunner {

    public static void main(String[] args) {

        new SpringApplicationBuilder(ThreadApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}