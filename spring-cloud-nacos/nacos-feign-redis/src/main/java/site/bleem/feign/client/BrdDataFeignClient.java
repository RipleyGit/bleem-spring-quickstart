package site.bleem.feign.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import site.bleem.feign.config.RequestInterceptorConfig;

import java.util.List;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@FeignClient(name = "broadcast-data",configuration = RequestInterceptorConfig.class)
public interface BrdDataFeignClient {

    @PostMapping(value = "/setEquip/setInfoPlayOrder")
    ResponseEntity settingEquipInfoPlayOrder(@RequestBody @Validated JSONObject paramVo) throws Exception;

}
