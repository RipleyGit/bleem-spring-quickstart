package site.bleem.boot.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .httpBasic()//使用basic登录
                .formLogin()//配置登录功能： 配置登录页面和登录请求的处理逻辑。
                .and().authorizeRequests()//定义访问权限规则： 配置哪些路径需要进行认证，哪些路径可以匿名访问。
                .anyRequest()//所有请求
                .authenticated()//授权
                .and().csrf().disable()//其他安全配置： 配置跨域请求、禁用或启用某些功能等。
        ;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
