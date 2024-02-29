package site.bleem.common.security.component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;
import site.bleem.common.security.entity.BleemUser;

public class BleemUserAuthenticationConverter implements UserAuthenticationConverter {
    private static final String N_A = "N/A";

    public BleemUserAuthenticationConverter() {
    }

    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap();
        response.put("user_name", authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }

        return response;
    }

    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey("user_name")) {
            Collection<? extends GrantedAuthority> authorities = this.getAuthorities(map);
            String username = (String)map.get("userName");
            Integer id = (Integer)map.get("userId");
            String empNo = (String)map.get("empNo");
            String cnName = (String)map.get("cnName");
            String roleInfos = (String)map.get("roleInfos");
            String deptInfos = (String)map.get("deptId");
            String orgId = (String)map.get("orgId");
            BleemUser user = new BleemUser(id, empNo, cnName, username, "N/A", roleInfos, deptInfos, orgId, true, true, true, true, authorities);
            return new UsernamePasswordAuthenticationToken(user, "N/A", authorities);
        } else {
            return null;
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get("authorities");
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String)authorities);
        } else if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString((Collection)authorities));
        } else {
            throw new IllegalArgumentException("Authorities must be either a String or a Collection");
        }
    }
}
