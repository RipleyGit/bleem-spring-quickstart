package site.bleem.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import site.bleem.common.security.component.Auth2ExceptionSerializer;

@JsonSerialize(
    using = Auth2ExceptionSerializer.class
)
public class ForbiddenException extends Auth2Exception {
    public ForbiddenException(String msg, Throwable t) {
        super(msg);
    }

    public String getOAuth2ErrorCode() {
        return "access_denied";
    }

    public int getHttpErrorCode() {
        return HttpStatus.FORBIDDEN.value();
    }
}
