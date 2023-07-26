package site.bleem.feign.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.UuidUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.feign.client.BrdDataFeignClient;
import site.bleem.feign.enums.WorkStatus;
import site.bleem.feign.service.RedisService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@RestController
public class HelloController {

    @Resource
    private RedisService redisService;

    @Resource
    private BrdDataFeignClient brdDataFeignClient;

    @GetMapping("/hello")
    public ResponseEntity<String> returnWorld() {
        return ResponseEntity.ok("world!");
    }

    @GetMapping("/hello/{content}")
    public ResponseEntity<String> settingEquipInfoPlayOrder(@PathVariable("content") @Validated String content) throws Exception {
        JSONObject key = new JSONObject();
        key.put("hid", "12345687");
        key.put("equipType", 270);
        key.put("port", 10005);
        key.put("ip", "172.22.6.73");
        JSONObject dto = new JSONObject();
        dto.put("uuid", UuidUtils.generateUuid());
        dto.put("functionType", 3);
        dto.put("dto", key);
        dto.put("cycleCount", 1);
        dto.put("infoContent", content);
        Object responseEntity = brdDataFeignClient.syncSettingEquipInfoPlayOrder(dto);
        while (true) {
            Map<String, Object> map = redisService.hmGet("pp_broad:register_equip_status_map");
            JSONObject parse = JSONObject.parseObject(JSONObject.toJSONString(map.get("12345687")));
            Integer devStatus = parse.getInteger("devStatus");
            WorkStatus status = WorkStatus.byCode(devStatus);
            System.out.println("当前设备状态："+status.getDesc());
            if (status == WorkStatus.FRR){
                dto.put("infoContent", "播放结束");
                brdDataFeignClient.syncSettingEquipInfoPlayOrder(dto);
                break;
            }
        }
        return ResponseEntity.ok("world!");
    }
}
