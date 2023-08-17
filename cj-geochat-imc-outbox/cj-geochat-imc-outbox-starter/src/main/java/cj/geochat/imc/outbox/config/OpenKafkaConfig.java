package cj.geochat.imc.outbox.config;

import cj.geochat.ability.kafka.annotation.EnableCjKafka;
import cj.geochat.imc.outbox.IOutboxFactory;
import cj.geochat.imc.outbox.factory.DefaultOutboxFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EnableCjKafka
@Configuration
public class OpenKafkaConfig {
    @Value("${inbox.topic.name}")
    String inboxTopicName;
    @Value("${inbox.topic.numPartitions}")
    int inboxNumPartitions;
    @Value("${inbox.topic.replicationFactor}")
    short inboxReplicationFactor;

    @Value("${outbox.topics.named-prefix}")
    String outboxNamedPrefix;
    @Value("${outbox.topics.topic-count}")
    int outboxTopicCount;
    @Value("${outbox.topics.numPartitions}")
    int outboxNumPartitions;
    @Value("${outbox.topics.replicationFactor}")
    short outboxReplicationFactor;

    @Bean
    public NewTopic inbox() {
        return new NewTopic(inboxTopicName, inboxNumPartitions, inboxReplicationFactor);
    }


    @Bean
    public IOutboxFactory outboxFactory(ConfigurableApplicationContext applicationContext) {
        List<NewTopic> outboxes = new ArrayList<>();
        for (var i = 0; i < outboxTopicCount; i++) {
            String name = String.format("%s%s", outboxNamedPrefix, i);
            var topic = new NewTopic(name, outboxNumPartitions, outboxReplicationFactor);
            applicationContext.getBeanFactory().registerSingleton(name, topic);
            outboxes.add(topic);
        }
        var factory = new DefaultOutboxFactory();
        factory.init(c->c.addAll(outboxes));
        return factory;
    }
}
