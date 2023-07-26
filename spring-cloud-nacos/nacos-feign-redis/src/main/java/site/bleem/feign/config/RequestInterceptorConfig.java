package site.bleem.feign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class RequestInterceptorConfig implements RequestInterceptor {
    public RequestInterceptorConfig() {
    }

    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("from", new String[]{"Y"});
    }
}
