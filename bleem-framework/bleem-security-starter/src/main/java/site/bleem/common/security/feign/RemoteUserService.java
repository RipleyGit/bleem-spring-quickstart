package site.bleem.common.security.feign;

import cn.hutool.system.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import site.bleem.common.core.result.Result;

@FeignClient(
    contextId = "remoteUserService",
    value = "imine-upms-biz"
)
public interface RemoteUserService {
    @GetMapping({"/user/info/{username}"})
    Result<UserInfo> info(@PathVariable("username") String var1, @RequestHeader("from") String var2);

    @GetMapping({"/social/info/{inStr}"})
    Result<UserInfo> social(@PathVariable("inStr") String var1);
}