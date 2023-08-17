package cj.geochat.imc.comet.consumer;

import cj.geochat.ability.util.GeochatRuntimeException;
import cj.geochat.imc.comet.IOutboxService;
import cj.geochat.imc.common.ImcFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

@Slf4j
public class OutboxConsumer implements AcknowledgingMessageListener<String, String> {
    IOutboxService outboxService;

    public OutboxConsumer(IOutboxService outboxService) {
        this.outboxService = outboxService;
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String data = record.value();
        try {
            var frame = ImcFrame.fromText(data);
            outboxService.outbox(frame);
        } catch (Exception e) {
            log.error("5000", e);
            throw new GeochatRuntimeException("5000", e);
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
