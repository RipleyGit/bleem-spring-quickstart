package site.bleem.common.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import site.bleem.common.core.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(ResourceAuthExceptionEntryPoint.class);
    private final ObjectMapper objectMapper;

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            Result<String> result = new Result<>();
            result.setCode(401);
            if (authException != null) {
                result.setMessage("error");
                result.setData(authException.getMessage());
            }

            response.setStatus(401);
            PrintWriter printWriter = response.getWriter();
            printWriter.append(this.objectMapper.writeValueAsString(result));
        } catch (Throwable var6) {
            throw var6;
        }
    }

    public ResourceAuthExceptionEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
