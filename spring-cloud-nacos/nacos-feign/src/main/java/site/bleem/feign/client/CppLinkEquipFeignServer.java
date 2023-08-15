package site.bleem.feign.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/8/7
 */
@FeignClient(contextId = "cppLinkEquipFeignServer", value = "link-service")
public interface CppLinkEquipFeignServer {
    @PostMapping("/api/v1/do_control")
    ResponseEntity doControl(@RequestBody JSONObject jsonObject);
}
