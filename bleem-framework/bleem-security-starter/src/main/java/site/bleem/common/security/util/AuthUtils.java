package site.bleem.common.security.util;

import cn.hutool.core.codec.Base64;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public final class AuthUtils {
    private static final Logger log = LoggerFactory.getLogger(AuthUtils.class);
    private static final String BASIC_ = "Basic ";

    public static String[] extractAndDecodeHeader(String header) throws UnsupportedEncodingException {
        try {
            byte[] base64Token = header.substring(6).getBytes("UTF-8");

            byte[] decoded;
            try {
                decoded = Base64.decode(base64Token);
            } catch (IllegalArgumentException var5) {
                throw new RuntimeException("Failed to decode basic authentication token");
            }

            String token = new String(decoded, "UTF-8");
            int delim = token.indexOf(":");
            if (delim == -1) {
                throw new RuntimeException("Invalid basic authentication token");
            } else {
                return new String[]{token.substring(0, delim), token.substring(delim + 1)};
            }
        } catch (Throwable var6) {
            throw var6;
        }
    }

    public static String[] extractAndDecodeHeader(HttpServletRequest request) throws UnsupportedEncodingException {
        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Basic ")) {
                return extractAndDecodeHeader(header);
            } else {
                throw new RuntimeException("请求头中client信息为空");
            }
        } catch (Throwable var2) {
            throw var2;
        }
    }

    private AuthUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
