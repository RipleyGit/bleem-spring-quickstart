package site.bleem.common.security.handler;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public abstract class AbstractAuthenticationFailureEvenHandler implements ApplicationListener<AbstractAuthenticationFailureEvent> {
    public AbstractAuthenticationFailureEvenHandler() {
    }

    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        AuthenticationException authenticationException = event.getException();
        Authentication authentication = (Authentication)event.getSource();
        this.handle(authenticationException, authentication);
    }

    public abstract void handle(AuthenticationException var1, Authentication var2);
}