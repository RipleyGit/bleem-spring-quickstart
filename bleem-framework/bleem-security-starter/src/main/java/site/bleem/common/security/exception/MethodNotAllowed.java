package site.bleem.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import site.bleem.common.security.component.Auth2ExceptionSerializer;

@JsonSerialize(
    using = Auth2ExceptionSerializer.class
)
public class MethodNotAllowed extends Auth2Exception {
    public MethodNotAllowed(String msg, Throwable t) {
        super(msg);
    }

    public String getOAuth2ErrorCode() {
        return "method_not_allowed";
    }

    public int getHttpErrorCode() {
        return HttpStatus.METHOD_NOT_ALLOWED.value();
    }
}