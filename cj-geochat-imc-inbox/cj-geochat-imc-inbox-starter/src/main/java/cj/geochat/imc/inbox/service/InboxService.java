package cj.geochat.imc.inbox.service;

import cj.geochat.imc.common.ImcFrame;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class InboxService implements IInboxService {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    NewTopic inbox;
    @Value("${inbox.topic.numPartitions}")
    int numPartitions;

    //    @Transactional
    @Override
    public void inbox(ImcFrame frame) {
        String channel = frame.channel().getChannel();
        String hex = DigestUtils.md2Hex(channel);
        int num = hex.hashCode() & Integer.MAX_VALUE;
        int indexPart = num % numPartitions;
        //同一channel发到同一分区上，这样保证消费者消息在同一channel中消息的顺序
        kafkaTemplate.send(inbox.name(), indexPart, channel, frame.toText());
    }
}
