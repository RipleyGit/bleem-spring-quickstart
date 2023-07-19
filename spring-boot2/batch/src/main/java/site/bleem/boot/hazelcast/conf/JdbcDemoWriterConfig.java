package site.bleem.boot.hazelcast.conf;

import site.bleem.boot.hazelcast.pojo.Doc;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JdbcDemoWriterConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("jdbcBatchItemWriter")
    private ItemWriter<Doc> itemWriter;

    @Autowired
    @Qualifier("elasticsearchDocItemReader")
    private ItemReader<Doc> itemReader;

    /**
     * job执行入口
     * @return
     */
    @Bean
    public Job dbOutputJob(){
        return jobBuilderFactory.get("dbOutputJob")
                .start(dbOutputStep())
                .build();
    }

    /**
     * 自定义job的执行步骤
     * @return
     */
    @Bean
    public Step dbOutputStep(){
        return stepBuilderFactory.get("dbOutputStep")
                .<Doc,Doc>chunk(10)   //设置每次操作的数据个数
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }

}

