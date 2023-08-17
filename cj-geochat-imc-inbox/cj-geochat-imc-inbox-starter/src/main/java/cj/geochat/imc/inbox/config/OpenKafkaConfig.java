package cj.geochat.imc.inbox.config;

import cj.geochat.ability.kafka.annotation.EnableCjKafka;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCjKafka
@Configuration
public class OpenKafkaConfig {
    @Value("${inbox.topic.name}")
    String topicName;
    @Value("${inbox.topic.numPartitions}")
    int numPartitions;
    @Value("${inbox.topic.replicationFactor}")
    short replicationFactor;

    @Bean
    public NewTopic inbox() {
        return new NewTopic(topicName, numPartitions, replicationFactor);
    }
}
