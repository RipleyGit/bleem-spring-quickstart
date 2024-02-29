package site.bleem.common.security.util;

import cn.hutool.core.util.StrUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import site.bleem.common.security.entity.BleemUser;

public final class SecurityUtils {
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static BleemUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        return principal instanceof BleemUser ? (BleemUser)principal : null;
    }

    public static BleemUser getUser() {
        Authentication authentication = getAuthentication();
        return authentication == null ? null : getUser(authentication);
    }

    public static List<Integer> getRoles() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<Integer> roleIds = new ArrayList();
        authorities.stream().filter((granted) -> {
            return StrUtil.startWith(granted.getAuthority(), "ROLE_");
        }).forEach((granted) -> {
            String id = StrUtil.removePrefix(granted.getAuthority(), "ROLE_");
            roleIds.add(Integer.parseInt(id));
        });
        return roleIds;
    }

    private SecurityUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
