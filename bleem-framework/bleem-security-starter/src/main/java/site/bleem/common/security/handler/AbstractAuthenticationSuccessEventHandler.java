package site.bleem.common.security.handler;

import cn.hutool.core.collection.CollUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

public abstract class AbstractAuthenticationSuccessEventHandler implements ApplicationListener<AuthenticationSuccessEvent> {
    public AbstractAuthenticationSuccessEventHandler() {
    }

    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = (Authentication)event.getSource();
        if (CollUtil.isNotEmpty(authentication.getAuthorities())) {
            this.handle(authentication);
        }

    }

    public abstract void handle(Authentication var1);
}
