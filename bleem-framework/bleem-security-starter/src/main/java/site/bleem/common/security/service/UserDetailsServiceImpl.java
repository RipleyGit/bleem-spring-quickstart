//package site.bleem.common.security.service;
//
//import cn.hutool.core.util.ArrayUtil;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Set;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//import site.bleem.common.security.entity.BleemUser;
//import site.bleem.common.security.exception.Auth2Exception;
//import site.bleem.common.security.feign.RemoteUserService;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
//    private final RemoteUserService remoteUserService;
//    private final CacheManager cacheManager;
//
//    public UserDetails loadUserByUsername(String username) {
//        try {
//            Cache cache = this.cacheManager.getCache("user_details");
//            if (cache != null && cache.get(username) != null) {
//                return (BleemUser)cache.get(username).get();
//            } else {
//                R<UserInfo> result = this.remoteUserService.info(username, "Y");
//                UserDetails userDetails = this.getUserDetails(result);
//                cache.put(username, userDetails);
//                return userDetails;
//            }
//        } catch (Throwable var5) {
//            throw var5;
//        }
//    }
//
//    private UserDetails getUserDetails(R<UserInfo> result) {
//        if (result != null && result.getData() != null) {
//            if (result != null && result.getCode() == 400) {
//                throw new Auth2Exception(result.getMsg(), String.valueOf(result.getCode()));
//            } else if (result != null && result.getCode() == 10001) {
//                throw new Auth2Exception(result.getMsg(), String.valueOf(result.getCode()));
//            } else if (result != null && result.getData() != null && (((UserInfo)result.getData()).getRoleList() == null || ((UserInfo)result.getData()).getRoleList().size() == 0)) {
//                throw new Auth2Exception("该账户未赋予权限，请联系管理员", "404");
//            } else {
//                UserInfo info = (UserInfo)result.getData();
//                User user = info.getUser();
//                Set<String> roleInfoSet = new HashSet();
//                Iterator var5 = info.getRoleList().iterator();
//
//                while(var5.hasNext()) {
//                    Role role = (Role)var5.next();
//                    roleInfoSet.add(role.getRoleId() + ":" + role.getRoleMakedId());
//                }
//
//                String roleInfos = StringUtils.join(roleInfoSet.toArray(), ";");
//                Set<String> dbAuthsSet = new HashSet();
//                if (ArrayUtil.isNotEmpty(info.getRoles())) {
//                    Arrays.stream(info.getRoles()).forEach((rolex) -> {
//                        dbAuthsSet.add("ROLE_" + rolex);
//                    });
//                    dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));
//                }
//
//                Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList((String[])dbAuthsSet.toArray(new String[0]));
//                String password = "{bcrypt}" + user.getPassword();
//                return new MineUser(user.getUserId(), user.getEmpNo(), user.getCnName(), user.getUserName(), password, roleInfos, info.getDeptInfos(), String.valueOf(user.getOrgId()), true, true, true, true, authorities);
//            }
//        } else {
//            throw new Auth2Exception("无效登录", "404");
//        }
//    }
//
//    public UserDetailsServiceImpl(RemoteUserService remoteUserService, CacheManager cacheManager) {
//        this.remoteUserService = remoteUserService;
//        this.cacheManager = cacheManager;
//    }
//}
