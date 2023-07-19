package cn.chainof.micro.controller;

import cn.chainof.micro.model.XmlProtocolAlarm;
import cn.chainof.micro.util.XmlBuilder;
import com.alibaba.fastjson.JSON;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/18
 */
@RestController
public class XmlFileController {
//    @ApiOperation(value = "本地文件上传到HDFS", nickname = "saveFileToHdfs", notes = "srcPath：本地文件路径；hdfsPath：HDFS文件路径", response = BooleanResp.class, tags={ "HDFS", })
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successful operation", response = BooleanResp.class) })
    @RequestMapping(value = "/localFile/upload",
            consumes = { "multipart/form-data" },
            method = RequestMethod.POST)
    ResponseEntity<String> saveFile(@RequestPart("file") MultipartFile xmlFile) throws Exception {
        String originalFilename = xmlFile.getOriginalFilename();
        File tempFile = File.createTempFile("tmp", ".xml");
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(xmlFile.getInputStream());
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(tempFile));
        // 跳过前言
        if (eventReader.peek().isStartDocument()) {
            eventReader.nextEvent();
        }
        // 将剩余的事件写入新文件
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            eventWriter.add(event);
        }
        eventReader.close();
        eventWriter.close();
        System.out.println("XML前言已成功删除");
        JAXBContext jaxbContext = JAXBContext.newInstance(XmlProtocolAlarm.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        XmlProtocolAlarm person = (XmlProtocolAlarm) jaxbUnmarshaller.unmarshal(tempFile);
        tempFile.deleteOnExit();
        //利用输入流获取XML文件内容
//        BufferedReader br = new BufferedReader(new InputStreamReader(, "UTF-8"));
//        StringBuffer buffer = new StringBuffer();
//        String line = "";
//        while ((line = br.readLine()) != null) {
//            buffer.append(line);
//        }
//        br.close();
//        //XML转为JAVA对象
//        XmlProtocolAlarm cityList = (XmlProtocolAlarm) XmlBuilder.xmlStrToObject(XmlProtocolAlarm.class, buffer.toString());
//        cityList.getAlarmList();
        return ResponseEntity.ok(JSON.toJSONString(person));
    }
}
