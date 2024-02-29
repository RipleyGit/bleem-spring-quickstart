package site.bleem.common.security.handler;

import cn.hutool.http.HttpUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class FormAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger log = LoggerFactory.getLogger(FormAuthenticationFailureHandler.class);

    public FormAuthenticationFailureHandler() {
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        try {
            log.debug("表单登录失败:{}", exception.getLocalizedMessage());
            response.sendRedirect(String.format("/token/login?error=%s", HttpUtil.encodeParams(exception.getMessage(), Charset.defaultCharset())));
        } catch (Throwable var5) {
            throw var5;
        }
    }
}