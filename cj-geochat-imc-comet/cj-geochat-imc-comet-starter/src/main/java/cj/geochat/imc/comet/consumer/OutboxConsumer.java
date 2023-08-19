package cj.geochat.imc.comet.consumer;

import cj.geochat.ability.util.GeochatRuntimeException;
import cj.geochat.imc.comet.IChannelDistributor;
import cj.geochat.imc.comet.annotation.CjChannelType;
import cj.geochat.imc.common.ImcChannel;
import cj.geochat.imc.common.ImcFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class OutboxConsumer implements AcknowledgingMessageListener<String, String> {
    Map<String, IChannelDistributor> channelDistributorMap;

    public OutboxConsumer(ApplicationContext applicationContext) {
        channelDistributorMap = new HashMap<>();
        Collection<IChannelDistributor> collection = applicationContext.getBeansOfType(IChannelDistributor.class).values();
        for (IChannelDistributor distributor : collection) {
            CjChannelType cjChannelType = distributor.getClass().getAnnotation(CjChannelType.class);
            if (cjChannelType == null) {
                throw new GeochatRuntimeException("500", String.format("分发器没有频道类型注解。%s", distributor.getClass().getName()));
            }
            channelDistributorMap.put(cjChannelType.value(), distributor);
        }
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String data = record.value();
        try {
            var frame = ImcFrame.fromText(data);
            ImcChannel channel = frame.channel();
            IChannelDistributor channelDistributor = channelDistributorMap.get(channel.getType());
            if (channelDistributor == null) {
                log.warn(String.format("不支持分发的频道类型:%s", channel.getType()));
                return;
            }
            channelDistributor.distribute(frame);
        } catch (Exception e) {
            log.error("5000", e);
            throw new GeochatRuntimeException("5000", e);
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
