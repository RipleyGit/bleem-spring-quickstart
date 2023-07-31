package site.bleem.boot.socket.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bleem.boot.socket.api.CmdControl;
import site.bleem.boot.socket.data.CmdData;
import site.bleem.boot.socket.enums.CmdResultCode;
import site.bleem.boot.socket.parser.BitOutput;
import site.bleem.boot.socket.parser.ParamNode;
import site.bleem.boot.socket.utils.UuidUtils;

import javax.annotation.Resource;


/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/25
 */
@RestController
public class HelloController {

    @Resource
    private CmdControl cmdControl;

    @GetMapping("/hello/{content}")
    public ResponseEntity<String> settingEquipInfoPlayOrder(@PathVariable("content") @Validated String content) throws Exception {

        String uuid = UuidUtils.generateUuid();
        String hid = "12345687";
        Integer equipType = 270;
        Integer port =10005;
        String ip = "172.22.6.73";
        Integer functionType = 3;
        ByteArrayOutputStream bs;
        bs = new ByteArrayOutputStream(1024);
        BitOutput output = new BitOutput(new BitOutput.StreamOutput(bs));
        output.writeUnsignedIntLE(4 * 8, 0x000B0060);
        output.writeUnsignedIntLE(1 * 8, 0x01);
        JSONObject paramJson = new JSONObject();
        paramJson.put("hid", hid);
        paramJson.put("devType", equipType);
        // 2022.5.5要求添加语速字段
        String speedConfig = "equipLinkageAlarmSpeed";//"equipPlayInfoSpeed";
//        if (FuctionTypeEnum.InfoType.THIRD_REL_ALARM.getCode() == dto.getFunctionType()) {
//            speedConfig = BaseConstants.EQUIP_LINKAGE_ALARM_SPEED;
//        } else if (FuctionTypeEnum.InfoType.NOWTIME.getCode() == dto.getFunctionType()) {
//            speedConfig = BaseConstants.EQUIP_SNOWTIME_SPEED;
//        }
        String speed = "4";
        paramJson.put("txtPlaySpeed", 4);
        paramJson.put("txtFucType", functionType);
        paramJson.put("cycleNum", 1);
        String byteContent = JSON.toJSON(content.getBytes("GB2312")).toString();
        JSONArray txtContentArray = JSONArray.parseArray(byteContent);
        paramJson.put("txtContent", txtContentArray);
        CmdData cmdData = new CmdData();
        cmdData.setEquIdentity(hid);
        CmdData.UdpConnectParam udpConnectParam = new CmdData.UdpConnectParam();
        udpConnectParam.setIp(ip);
        udpConnectParam.setPort(port);
        cmdData.setUdpConnectParam(udpConnectParam);
        JSONArray protocolConfig = JSONArray.parseArray("[{\"size\": \"1\", \"fixed\": \"2\", \"dtName\": \"字符串\", \"endian\": 0, \"binType\": \"2\", \"fixList\": [{\"fixId\": \"1\", \"fixName\": \"定长\"}, {\"fixId\": \"2\", \"fixName\": \"不定长\"}], \"dataType\": 3, \"fieldName\": \"hid\", \"binTypeList\": [{\"typeId\": \"2\", \"typeName\": \"BCD\"}, {\"typeId\": \"3\", \"typeName\": \"Ascii\"}, {\"typeId\": \"4\", \"typeName\": \"Hex\"}]}, {\"size\": \"2\", \"dtName\": \"数值\", \"endian\": 0, \"binType\": \"3\", \"dataType\": 2, \"fieldName\": \"devType\", \"binTypeList\": [{\"typeId\": \"1\", \"typeName\": \"BIT\"}, {\"typeId\": \"3\", \"typeName\": \"BYTE\"}]}, {\"size\": \"3\", \"dtName\": \"保留\", \"binType\": \"3\", \"dataType\": 5, \"fieldName\": \"reserve_551952\", \"binTypeList\": [{\"typeId\": \"1\", \"typeName\": \"BIT\"}, {\"typeId\": \"3\", \"typeName\": \"BYTE\"}]}, {\"size\": \"1\", \"dtName\": \"数值\", \"endian\": 0, \"binType\": \"3\", \"dataType\": 2, \"fieldName\": \"txtPlaySpeed\", \"binTypeList\": [{\"typeId\": \"1\", \"typeName\": \"BIT\"}, {\"typeId\": \"3\", \"typeName\": \"BYTE\"}]}, {\"size\": \"1\", \"dtName\": \"数值\", \"endian\": 0, \"binType\": \"3\", \"dataType\": 2, \"fieldName\": \"txtFucType\", \"binTypeList\": [{\"typeId\": \"1\", \"typeName\": \"BIT\"}, {\"typeId\": \"3\", \"typeName\": \"BYTE\"}]}, {\"size\": \"1\", \"dtName\": \"数值\", \"endian\": 0, \"binType\": \"3\", \"dataType\": 2, \"fieldName\": \"cycleNum\", \"binTypeList\": [{\"typeId\": \"1\", \"typeName\": \"BIT\"}, {\"typeId\": \"3\", \"typeName\": \"BYTE\"}]}, {\"size\": \"2\", \"fixed\": \"2\", \"dtName\": \"字符串\", \"endian\": 0, \"binType\": \"4\", \"fixList\": [{\"fixId\": \"1\", \"fixName\": \"定长\"}, {\"fixId\": \"2\", \"fixName\": \"不定长\"}], \"dataType\": 3, \"fieldName\": \"txtContent\", \"binTypeList\": [{\"typeId\": \"2\", \"typeName\": \"BCD\"}, {\"typeId\": \"3\", \"typeName\": \"Ascii\"}, {\"typeId\": \"4\", \"typeName\": \"Hex\"}]}]");
        String mainDataType = "0xE0";
        String subDataType = "0x01";
        // 数据转换
        ParamNode.doControl(paramJson, protocolConfig, output);
        byte[] data = bs.toByteArray();
        Integer iMainDataType = Integer.parseInt(mainDataType.substring(2, mainDataType.length()), 16);
        Integer iSubDataType = Integer.parseInt(subDataType.substring(2, subDataType.length()), 16);
        //发送指令
        cmdData.setUuid(uuid);
        cmdData.setMainCode(iMainDataType);
        cmdData.setSubCode(iSubDataType);
        cmdData.setData(data);
        cmdData.setResponseTimeoutSecond(3);
        cmdData.setRouting(0);
        cmdData.setProtocolType(2);//2,"UDP协议"
        final CmdResultCode responseCode = cmdControl.send(cmdData);

        return ResponseEntity.ok(content);
    }
}
