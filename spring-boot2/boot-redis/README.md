# spring-nacos学习

## 启动异常

- Caused by: java.lang.ClassNotFoundException: org.springframework.util.unit.DataSize
  出现 java.lang.ClassNotFoundException: org.springframework.util.unit.DataSize 异常是因为缺少相应的依赖包。DataSize
  类是在Spring Framework 5.1版本中引入的，需要引入相应的依赖包才能使用。
  如果你使用的是Maven来管理项目依赖，可以在项目的pom.xml文件中添加以下依赖：

```agsl
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>5.1.0.RELEASE</version>
</dependency>
```

- java.lang.IllegalStateException: failed to req API:/nacos/v1/ns/service/list after all servers([172.21.34.104:8848])
  tried: failed to req API:http://172.21.34.104:8848/nacos/v1/ns/service/list. code:403 msg: <html><body><h1>Whitelabel
  Error Page</h1><p>This application has no explicit mapping for /error, so you are seeing this as a
  fallback.</p><div id='created'>Tue Jul 25 14:11:28 CST 2023</div><div>There was an unexpected error (type=Forbidden,
  status=403).</div><div>unknown user!</div></body></html>
  版本不支持：需要升级到nacos 2.x版本
- Param 'serviceName' is illegal, serviceName is blank
  spring-cloud-dependencies 2020.0.0 默认不在加载bootstrap 配置文件，如果项目中要用bootstrap 配置文件 需要手动添加spring-cloud-starter-bootstrap 依赖，不然启动项目会报错的。
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```
##  