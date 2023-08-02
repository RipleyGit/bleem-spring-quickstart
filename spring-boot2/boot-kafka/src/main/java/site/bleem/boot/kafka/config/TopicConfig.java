package site.bleem.boot.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConditionalOnProperty(
    name = {"kafka.enable"}
)
@Configuration
@ConfigurationProperties(
    prefix = "kafka"
)
public class TopicConfig {
    private List<Topic> topics;

    public TopicConfig() {
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Topic> getTopics() {
        return this.topics;
    }

    public String toString() {
        return "TopicConfig(topics=" + this.getTopics() + ")";
    }

    static class Topic {
        String name;
        Integer numPartitions = 4;
        Short replicationFactor = Short.valueOf((short)3);

        Topic() {
        }

        NewTopic toNewTopic() {
            return new NewTopic(this.name, this.numPartitions, this.replicationFactor);
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNumPartitions(Integer numPartitions) {
            this.numPartitions = numPartitions;
        }

        public void setReplicationFactor(Short replicationFactor) {
            this.replicationFactor = replicationFactor;
        }

        public String getName() {
            return this.name;
        }

        public Integer getNumPartitions() {
            return this.numPartitions;
        }

        public Short getReplicationFactor() {
            return this.replicationFactor;
        }

        public String toString() {
            return "TopicConfig.Topic(name=" + this.getName() + ", numPartitions=" + this.getNumPartitions() + ", replicationFactor=" + this.getReplicationFactor() + ")";
        }
    }
}
