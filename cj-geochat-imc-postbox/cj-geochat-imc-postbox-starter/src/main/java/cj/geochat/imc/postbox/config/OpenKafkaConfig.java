package cj.geochat.imc.postbox.config;

import cj.geochat.ability.kafka.annotation.EnableCjKafka;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCjKafka
@Configuration
public class OpenKafkaConfig {
    @Value("${postbox.topic-config.numPartitions}")
    int inboxNumPartitions;
    @Value("${postbox.topic-config.replicationFactor}")
    short inboxReplicationFactor;

    @Value("${postbox.consumer.event}")
    String eventTopic;
    @Value("${postbox.consumer.offline}")
    String offlineTopic;

    @Bean
    public NewTopic event() {
        return new NewTopic(eventTopic, inboxNumPartitions, inboxReplicationFactor);
    }

    @Bean
    public NewTopic offline() {
        return new NewTopic(offlineTopic, inboxNumPartitions, inboxReplicationFactor);
    }


}
