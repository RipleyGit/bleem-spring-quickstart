package site.bleem.boot.hazelcast.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class RequestResultFilter implements Filter {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private HttpSession httpSession;

    private final static String HYSTRIX_CONTEXT = "hystrix_context";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HystrixContext.getInstance();

//        HystrixRequestContext context = null;
//        if (httpSession.getAttribute(HYSTRIX_CONTEXT) == null) {
//            context = HystrixRequestContext.initializeContext();
//        } else {
//            context = (HystrixRequestContext) request.getAttribute(HYSTRIX_CONTEXT);
//        }
//        try {
//        } finally {
//            httpSession.setAttribute(HYSTRIX_CONTEXT,context);
//        }
            chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}