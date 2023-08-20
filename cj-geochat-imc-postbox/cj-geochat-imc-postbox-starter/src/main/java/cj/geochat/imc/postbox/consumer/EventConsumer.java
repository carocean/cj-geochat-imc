package cj.geochat.imc.postbox.consumer;

import cj.geochat.ability.util.GeochatException;
import cj.geochat.imc.common.ImcChannel;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.postbox.IGeolocationEventService;
import cj.geochat.imc.postbox.ILinestateEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventConsumer {
    @Autowired
    ILinestateEventService linestateEventService;
    @Autowired
    IGeolocationEventService geolocationEventService;
    @KafkaListener(topics = "${postbox.consumer.event}", groupId = "postbox-event-group")
    public void consumeMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws Throwable {
        String data = record.value();
        try {
            var frame = ImcFrame.fromText(data);
            ImcChannel channel=frame.channel();
            if(channel.equals("geo","location")){
                geolocationEventService.update(frame);
            }
            if(channel.equals("sys","linestate")){
                linestateEventService.update(frame);
            }
        } catch (Exception e) {
            log.error("5000", e);
            throw new GeochatException("5000", e);
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
