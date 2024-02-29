package site.bleem.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import site.bleem.common.security.component.Auth2ExceptionSerializer;

@JsonSerialize(
    using = Auth2ExceptionSerializer.class
)
public class InvalidException extends Auth2Exception {
    public InvalidException(String msg, Throwable t) {
        super(msg);
    }

    public String getOAuth2ErrorCode() {
        return "invalid_exception";
    }

    public int getHttpErrorCode() {
        return 426;
    }
}