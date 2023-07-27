package site.bleem.boot.hazelcast;

import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@EnableScheduling
@SpringBootApplication
public class ShedlocalApp implements CommandLineRunner {

    public static void main(String[] args) {

        new SpringApplicationBuilder(ShedlocalApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

    @Value("${server.port:8081}")
    private String portal;


    @Scheduled(cron = "0/2 * * * * ?")
//    @Scheduled(fixedDelayString = "5000")
    @SchedulerLock(name = "channelCronName", lockAtMostFor = 5 * 1000)
    public void channelCron() {
        System.out.println(portal + "-定时任务：" + getCurrentDate());
    }

    /**
     * 获取系统时间 转换为yyyy-MM-dd HH:mm:ss格式,返回Date类型
     *
     * @return
     * @throws ParseException
     */
    public static String getCurrentDate() {
        String date = null;
        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datea = new Date();
        date = yyyyMMddHHmmss.format(datea);
        return date;

    }
}