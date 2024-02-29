package site.bleem.common.security.component;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@ConfigurationPropertiesScan
@ComponentScan({"site.bleem.common.security"})
public class ResourceServerAutoConfiguration {
    public ResourceServerAutoConfiguration() {
    }

    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate lbRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            public void handleError(ClientHttpResponse response) throws IOException {
                try {
                    if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
                        super.handleError(response);
                    }

                } catch (Throwable var3) {
                    throw var3;
                }
            }
        });
        return restTemplate;
    }
}
