package site.bleem.common.security.entity;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class BleemUser extends User implements Serializable {
    private static final long serialVersionUID = 4356645466223450416L;
    private Integer userId;
    private String empNo;
    private String cnName;
    private String roleInfos;
    private String deptInfos;
    private String orgId;

    public BleemUser(Integer userId, String empNo, String cnName, String username, String password, String roleInfos, String deptInfos, String orgId, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.empNo = empNo;
        this.cnName = cnName;
        this.roleInfos = roleInfos;
        this.deptInfos = deptInfos;
        this.orgId = orgId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public void setRoleInfos(String roleInfos) {
        this.roleInfos = roleInfos;
    }

    public void setDeptInfos(String deptInfos) {
        this.deptInfos = deptInfos;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getEmpNo() {
        return this.empNo;
    }

    public String getCnName() {
        return this.cnName;
    }

    public String getRoleInfos() {
        return this.roleInfos;
    }

    public String getDeptInfos() {
        return this.deptInfos;
    }

    public String getOrgId() {
        return this.orgId;
    }
}
