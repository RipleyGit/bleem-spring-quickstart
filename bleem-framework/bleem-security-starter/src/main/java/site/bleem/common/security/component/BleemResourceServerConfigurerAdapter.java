package site.bleem.common.security.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.client.RestTemplate;

public class BleemResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(BleemResourceServerConfigurerAdapter.class);
    @Autowired
    protected ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;
    @Autowired
    protected RemoteTokenServices remoteTokenServices;
    @Autowired
    private AccessDeniedHandler mineAccessDeniedHandler;
    @Autowired
    private PermitAllUrlProperties permitAllUrl;
    @Autowired
    private RestTemplate lbRestTemplate;

    public BleemResourceServerConfigurerAdapter() {
    }

    public void configure(HttpSecurity httpSecurity) throws Exception {
        try {
            httpSecurity.headers().frameOptions().disable();
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();
            this.permitAllUrl.getUrls().forEach((url) -> {
                ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)registry.antMatchers(new String[]{url})).permitAll();
            });
            ((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)registry.anyRequest()).authenticated().and()).csrf().disable();
        } catch (Throwable var3) {
            throw var3;
        }
    }

    public void configure(ResourceServerSecurityConfigurer resources) {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userTokenConverter = new BleemUserAuthenticationConverter();
        accessTokenConverter.setUserTokenConverter(userTokenConverter);
        this.remoteTokenServices.setRestTemplate(this.lbRestTemplate);
        this.remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
        resources.authenticationEntryPoint(this.resourceAuthExceptionEntryPoint).accessDeniedHandler(this.mineAccessDeniedHandler).tokenServices(this.remoteTokenServices);
    }
}
