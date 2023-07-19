package site.bleem.boot.hazelcast;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
 
@SpringBootApplication(exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
@EnableElasticsearchRepositories(basePackages = "cn.chainof.micro.es")
@EnableJpaRepositories(basePackages = "cn.chainof.micro.jpa")
public class EsEtlApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(EsEtlApplication.class, args);
    }
}