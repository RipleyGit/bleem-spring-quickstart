package site.bleem.boot.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DefaultUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private PasswordEncoder passwordEncoder;

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.userdetails.UserDetailsService#
     * loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("表单登录用户名:" + username);
        return buildUser(username);
    }

    private UserDetails buildUser(String username) {
        String password = passwordEncoder.encode("123456");
        logger.info("用户加密密码是:" + password);
        return new User(username, //用户名
                password,//用户密码
                true, //账户是否可用（用户是否被删）
                true, //账户没有过期
                true, //密码没有过期
                true,//账户没有冻结
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin")//赋予admin权限
        );
//        String passwd = "";
//        System.out.println("收到的账号"+username);
//        if (CheckFormat.isEmail(username)){
//            passwd = userService.selectPasswdByEmail(username);
//        }else if (CheckFormat.isPhone(username)){
//            passwd = userService.selectPasswdByPhone(username);
//        }else {
//            throw new RuntimeException("登录账号不存在");
//        }
//        System.out.println("查到的密码"+passwd);
//        return new User(username, passwd, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }

}