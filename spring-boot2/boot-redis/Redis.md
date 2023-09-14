# REDIS

## 依赖
```xml
        <!--redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
```

## FQA
- Unable to start web server; nested exception is org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat
A:程序未加载到bootstrap.yml配置文件，新建一个application.yml配置文件即可。