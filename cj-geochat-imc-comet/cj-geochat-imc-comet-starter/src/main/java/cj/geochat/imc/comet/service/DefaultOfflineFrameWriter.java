package cj.geochat.imc.comet.service;

import cj.geochat.imc.comet.IOfflineFrameWriter;
import cj.geochat.imc.comet.IOnlineTableManager;
import cj.geochat.imc.common.ImcFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DefaultOfflineFrameWriter implements IOfflineFrameWriter {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${comet.offline-topic.name}")
    String offlineTopicName;
    @Autowired
    IOnlineTableManager onlineTableManager;

    @Override
    public void write(String user, ImcFrame frame) {
        //看看用户在线不，如果在线则丢弃消息，因为用户所在的comet能发消息给客户。如果不在线则写离线消息
        if (onlineTableManager.isOnline(user)) {
            return;
        }
        frame.header("Offline-User",user);
        kafkaTemplate.send(offlineTopicName, user, frame.toText());
    }
}
