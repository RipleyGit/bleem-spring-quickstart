package site.bleem.common.security.component;

import cn.hutool.core.util.StrUtil;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

@Component("pms")
public class PermissionService {
    private static final Logger log = LoggerFactory.getLogger(PermissionService.class);

    public PermissionService() {
    }

    public boolean hasPermission(String permission) {
        if (StrUtil.isBlank(permission)) {
            return false;
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return false;
            } else {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                boolean result = authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText).anyMatch((x) -> {
                    return PatternMatchUtils.simpleMatch(permission, x);
                });
                return result;
            }
        }
    }
}
