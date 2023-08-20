package cj.geochat.imc.postbox.consumer;

import cj.geochat.ability.util.GeochatException;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.postbox.IOfflineFrameService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OfflineConsumer {
    @Autowired
    IOfflineFrameService offlineService;

    ///一定固定组名，因为如果存在多个收件节点，每个消息只能被一个接收，即单播。
    @KafkaListener(topics = "${postbox.consumer.offline}", groupId = "postbox-offline-group")
    public void consumeMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws Throwable {
        String data = record.value();
        try {
            var frame = ImcFrame.fromText(data);
            offlineService.write(frame);
        } catch (Exception e) {
            log.error("5000", e);
            throw new GeochatException("5000", e);
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
