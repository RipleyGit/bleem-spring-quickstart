package site.bleem.boot.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

@ConditionalOnProperty(
    name = {"kafka.enable"}
)
@Configuration
public class TopicManage {
    private final TopicConfig topicConfig;
    private final GenericWebApplicationContext context;

    @PostConstruct
    public void init() {
        this.initializeBeans(this.topicConfig.getTopics());
    }

    private void initializeBeans(List<TopicConfig.Topic> topics) {
        topics.forEach((t) -> {
            this.context.registerBean(t.name, NewTopic.class, t::toNewTopic, new BeanDefinitionCustomizer[0]);
        });
    }

    public TopicManage(TopicConfig topicConfig, GenericWebApplicationContext context) {
        this.topicConfig = topicConfig;
        this.context = context;
    }
}
