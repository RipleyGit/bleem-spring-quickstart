package site.bleem.common.security.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import site.bleem.common.security.exception.*;

public class BleemWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    private static final Logger log = LoggerFactory.getLogger(BleemWebResponseExceptionTranslator.class);
    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    public BleemWebResponseExceptionTranslator() {
    }

    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        try {
            Throwable[] causeChain = this.throwableAnalyzer.determineCauseChain(e);
            Exception exception = (AuthenticationException)this.throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
            if (exception != null) {
                return this.handleOAuth2Exception(new UnauthorizedException(e.getMessage(), e));
            } else {
                Exception ase = (AccessDeniedException)this.throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
                if (ase != null) {
                    return this.handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
                } else {
                     ase = (InvalidGrantException)this.throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class, causeChain);
                    if (ase != null) {
                        return this.handleOAuth2Exception(new InvalidException(ase.getMessage(), ase));
                    } else {
                         ase = (HttpRequestMethodNotSupportedException)this.throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
                        if (ase != null) {
                            return this.handleOAuth2Exception(new MethodNotAllowed(ase.getMessage(), ase));
                        } else {
                             ase = (OAuth2Exception)this.throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
                            return ase != null ? this.handleOAuth2Exception((OAuth2Exception)ase) : this.handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));
                        }
                    }
                }
            }
        } catch (Throwable var4) {
            throw var4;
        }
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || e instanceof InsufficientScopeException) {
            headers.set("WWW-Authenticate", String.format("%s %s", "Bearer", e.getSummary()));
        }

        return e instanceof ClientAuthenticationException ? new ResponseEntity(e, headers, HttpStatus.valueOf(status)) : new ResponseEntity(new Auth2Exception(e.getMessage(), e.getOAuth2ErrorCode()), headers, HttpStatus.valueOf(status));
    }
}
