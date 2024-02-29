package site.bleem.common.jdbc;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(
    proxyBeanMethods = false
)
public class JdbcAutoConfiguration {
    public JdbcAutoConfiguration() {
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
