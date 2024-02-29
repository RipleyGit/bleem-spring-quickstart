package site.bleem.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import site.bleem.common.security.component.Auth2ExceptionSerializer;

@JsonSerialize(
    using = Auth2ExceptionSerializer.class
)
public class UnauthorizedException extends Auth2Exception {
    public UnauthorizedException(String msg, Throwable t) {
        super(msg);
    }

    public String getOAuth2ErrorCode() {
        return "unauthorized";
    }

    public int getHttpErrorCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}