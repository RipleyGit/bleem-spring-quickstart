package site.bleem.common.security.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import site.bleem.common.core.result.Result;

import java.util.Map;

@FeignClient(
    contextId = "remoteTokenService",
    value = "imine-auth"
)
public interface RemoteTokenService {
    @PostMapping({"/token/page"})
    Result getTokenPage(@RequestBody Map<String, Object> var1, @RequestHeader("from") String var2);

    @DeleteMapping({"/token/{token}"})
    Result<Boolean> removeToken(@PathVariable("token") String var1, @RequestHeader("from") String var2);
}