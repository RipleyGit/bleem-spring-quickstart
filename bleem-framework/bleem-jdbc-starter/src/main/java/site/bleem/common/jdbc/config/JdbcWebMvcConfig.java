package site.bleem.common.jdbc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import java.util.List;

@Configuration
@ConditionalOnWebApplication(
    type = Type.SERVLET
)
public class JdbcWebMvcConfig implements WebMvcConfigurer {
    public JdbcWebMvcConfig() {
    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SqlFilterArgumentResolver());
    }
}
