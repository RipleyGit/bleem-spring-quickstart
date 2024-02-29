package site.bleem.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import site.bleem.common.security.component.Auth2ExceptionSerializer;

@JsonSerialize(
    using = Auth2ExceptionSerializer.class
)
public class Auth2Exception extends OAuth2Exception {
    private String errorCode;

    public Auth2Exception(String msg) {
        super(msg);
    }

    public Auth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
