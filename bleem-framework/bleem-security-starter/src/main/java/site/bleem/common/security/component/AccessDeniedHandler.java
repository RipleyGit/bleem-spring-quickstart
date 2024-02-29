package site.bleem.common.security.component;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.stereotype.Component;
import site.bleem.common.core.exception.DeniedException;
import site.bleem.common.core.result.Result;

@Component
public class AccessDeniedHandler extends OAuth2AccessDeniedHandler {
    private static final Logger log = LoggerFactory.getLogger(AccessDeniedHandler.class);
    private final ObjectMapper objectMapper;

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException {
        try {
            log.info("授权失败，禁止访问 {}", request.getRequestURI());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            Result result = Result.failed(new DeniedException("授权失败，禁止访问"));
            response.setStatus(403);
            PrintWriter printWriter = response.getWriter();
            printWriter.append(this.objectMapper.writeValueAsString(result));
        } catch (Throwable var6) {
            throw var6;
        }
    }

    public AccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
