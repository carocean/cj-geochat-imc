package cj.geochat.imc.comet.config;

import cj.geochat.ability.kafka.annotation.EnableCjKafka;
import cj.geochat.imc.comet.ICometConsumerFactory;
import cj.geochat.imc.comet.consumer.OutboxConsumer;
import com.github.f4b6a3.ulid.UlidCreator;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.ArrayList;
import java.util.List;

@EnableCjKafka
@Configuration
public class OpenKafkaConfig {
    @Value("${comet.offline-topic.name}")
    String offlineTopicName;
    @Value("${comet.offline-topic.numPartitions}")
    int offlineNumPartitions;
    @Value("${comet.offline-topic.replicationFactor}")
    short offlineReplicationFactor;

    @Value("${comet.event-topic.name}")
    String eventTopicName;
    @Value("${comet.event-topic.numPartitions}")
    int eventNumPartitions;
    @Value("${comet.event-topic.replicationFactor}")
    short eventReplicationFactor;

    @Value("${comet.connect-outbox-topic}")
    String outboxTopicName;

    @Bean
    public NewTopic offline() {
        return new NewTopic(offlineTopicName, offlineNumPartitions, offlineReplicationFactor);
    }

    @Bean
    public NewTopic event() {
        return new NewTopic(eventTopicName, eventNumPartitions, eventReplicationFactor);
    }

    @Bean
    public ICometConsumerFactory createCometConsumerFactory(
            ConcurrentKafkaListenerContainerFactory<String, String> factory,
            ConfigurableApplicationContext applicationContext) {
        List<ConcurrentMessageListenerContainer<String, String>> containers = new ArrayList<>();
        String group = outboxTopicName+"-" + UlidCreator.getUlid().toLowerCase() + "-group";
        ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(outboxTopicName);
        container.getContainerProperties().setMessageListener(new OutboxConsumer(applicationContext));
        container.getContainerProperties().setGroupId(group);
        container.setBeanName(group);
        container.start();
        containers.add(container);
        applicationContext.getBeanFactory().registerSingleton(group, container);
        return (c -> {
            c.customize(containers);
        });
    }
}
