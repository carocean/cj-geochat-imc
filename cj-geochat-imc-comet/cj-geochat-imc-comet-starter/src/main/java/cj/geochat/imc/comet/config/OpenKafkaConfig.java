package cj.geochat.imc.comet.config;

import cj.geochat.ability.kafka.annotation.EnableCjKafka;
import cj.geochat.imc.comet.ICometConsumerFactory;
import cj.geochat.imc.comet.IOutboxService;
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

    @Value("${comet.consumers.topics}")
    String[] outboxTopicNames;

    @Bean
    public NewTopic offline() {
        return new NewTopic(offlineTopicName, offlineNumPartitions, offlineReplicationFactor);
    }

    @Bean
    public ICometConsumerFactory createCometConsumerFactory(
            ConcurrentKafkaListenerContainerFactory<String, String> factory,
            ConfigurableApplicationContext applicationContext,
            IOutboxService outboxService) {
        List<ConcurrentMessageListenerContainer<String, String>> containers = new ArrayList<>();
        for (String topic : outboxTopicNames) {
            String group = "outbox-0-" + UlidCreator.getUlid().toLowerCase() + "_group";
            ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(topic);
            container.getContainerProperties().setMessageListener(new OutboxConsumer(outboxService));
            container.getContainerProperties().setGroupId(group);
            container.setBeanName(group);
            container.start();
            containers.add(container);
            applicationContext.getBeanFactory().registerSingleton(group, container);
        }
        return (c -> {
            c.customize(containers);
        });
    }
}
