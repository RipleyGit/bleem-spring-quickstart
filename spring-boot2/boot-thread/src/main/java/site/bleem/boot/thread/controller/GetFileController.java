package site.bleem.boot.thread.controller;


import site.bleem.boot.thread.client.OpenFeginClient;
import com.alibaba.fastjson.JSONArray;
import feign.Response;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@RestController
public class GetFileController {

    @Autowired
    private OpenFeginClient openFeginClient;

    @GetMapping("/get/file")
    public ResponseEntity<byte[]> getFile() throws IOException {

        File earFile = new File("/Users/yubs/project/personal/spring-study/spring-http-file/src/main/resources/train.zip");
        byte[] bytes = FileUtils.readFileToByteArray(earFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition: form-data", "name=\"attachment\"; filename*=UTF-8''" + URLEncoder.encode("zzzzz", "UTF-8") + ".zip");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

    }

    @GetMapping("/get/fegin")
    public ResponseEntity<byte[]> getfegin() throws IOException {

        Response response = openFeginClient.getFile();

        JSONArray array = getTableMapping();

        //获得文件流
        Response.Body body = response.body();
        InputStream inputStream = null;
        String filePath = System.getProperty("java.io.tmpdir") +(int)(1+Math.random()*(10-1+1))+".zip";
        Path path = Paths.get(filePath);
        File file = path.toFile();
        try {
            FileUtils.copyInputStreamToFile(body.asInputStream(),file);
            ZipFile zf = new ZipFile(filePath);
            ZipInputStream zin = new ZipInputStream(new FileInputStream(filePath));
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null){
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int rc = 0;
                inputStream = zf.getInputStream(ze);
                while ((rc = inputStream.read(buff, 0, 1024)) > 0) {
                    byteArrayOutputStream.write(buff, 0, rc);
                }
                byte[] bytes = byteArrayOutputStream.toByteArray();
                Object deserialize = SerializationUtils.deserialize(bytes);
                array.add(byteArrayOutputStream.toByteArray());
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }finally {
            file.deleteOnExit();
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File earFile = new File("/Users/yubs/project/personal/spring-study/spring-http-file/src/main/resources/testCase9.pkg");
        byte[] bytes = FileUtils.readFileToByteArray(earFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition: form-data", "name=\"attachment\"; filename*=UTF-8''" + URLEncoder.encode("zzzzz", "UTF-8") + ".zip");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

    }


    @Autowired
    private ResourceLoader resourceLoader;

    public JSONArray getTableMapping() {
        JSONArray tableArray = new JSONArray();
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources
                    ("classpath:/train/*.pkg");
            for (Resource resource : resources) {

                File file = resource.getFile();
                byte[] bytes = FileUtils.readFileToByteArray(file);
                Object deserialize = SerializationUtils.deserialize(bytes);
                tableArray.add(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableArray;
    }
}
