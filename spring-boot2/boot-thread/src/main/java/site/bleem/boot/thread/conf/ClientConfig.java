package site.bleem.boot.thread.conf;

import site.bleem.boot.thread.client.OpenFeginClient;
import feign.Feign;
import feign.Logger;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ClientConfig {

    @Bean
    public OpenFeginClient initBigDataBridgeClient() {
        return Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .target(OpenFeginClient.class, "http://localhost:9001/");
    }

}
