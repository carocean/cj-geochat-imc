package cj.geochat.imc.outbox.factory;

import cj.geochat.imc.common.Customizer;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.outbox.IOutboxFactory;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultOutboxFactory implements IOutboxFactory {
    List<NewTopic> outboxes;
    @Resource
    KafkaTemplate<String, String> kafkaTemplate;
    @Value("${outbox.topics.numPartitions}")
    int outboxNumPartitions;

    @Override
    public void init(Customizer<List<NewTopic>> customizer) {
        outboxes = new CopyOnWriteArrayList<>();
        customizer.customize(outboxes);
    }

    @Override
    public void unicast(ImcFrame frame) {
        String channel = frame.channel().getChannel();
        String hex = DigestUtils.md2Hex(channel);
        int num = hex.hashCode() & Integer.MAX_VALUE;
        int indexKey = num % outboxes.size();
        NewTopic topic = outboxes.get(indexKey);
        int indexPart = num % outboxNumPartitions;
        kafkaTemplate.send(topic.name(), indexPart, channel, frame.toText());
    }

    @Override
    public void broadcast(ImcFrame frame) {
        for (var topic:outboxes) {
            var copy=frame.copy();
            String channel = copy.channel().getChannel();
            String hex = DigestUtils.md2Hex(channel);
            int num = hex.hashCode() & Integer.MAX_VALUE;
            int indexPart = num % outboxNumPartitions;
            kafkaTemplate.send(topic.name(), indexPart, channel, copy.toText());
        }
    }
}
