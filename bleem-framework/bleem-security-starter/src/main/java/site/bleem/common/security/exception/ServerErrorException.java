package site.bleem.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import site.bleem.common.security.component.Auth2ExceptionSerializer;

@JsonSerialize(
    using = Auth2ExceptionSerializer.class
)
public class ServerErrorException extends Auth2Exception {
    public ServerErrorException(String msg, Throwable t) {
        super(msg);
    }

    public String getOAuth2ErrorCode() {
        return "server_error";
    }

    public int getHttpErrorCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
