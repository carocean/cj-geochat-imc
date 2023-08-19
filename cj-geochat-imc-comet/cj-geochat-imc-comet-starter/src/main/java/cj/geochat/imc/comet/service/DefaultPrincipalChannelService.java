package cj.geochat.imc.comet.service;

import cj.geochat.ability.oauth.app.principal.DefaultAppPrincipal;
import cj.geochat.imc.comet.IPrincipalChannelService;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.common.ImcSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DefaultPrincipalChannelService implements IPrincipalChannelService {
    @Value("${comet.event-topic.name}")
    String eventTopicName;
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void online(DefaultAppPrincipal principal) {
        ImcSender sender = ImcSender.toSender(principal.getAppid(), principal.getName(), principal.getAccount());
        ImcFrame frame = new ImcFrame(String.format("put /?linestate=online imc/1.0"), String.format("%s@linestate.sys", sender.toDescriptor()));
        kafkaTemplate.send(eventTopicName, frame.toText());
    }

    @Override
    public void offline(DefaultAppPrincipal principal) {
        ImcSender sender = ImcSender.toSender(principal.getAppid(), principal.getName(), principal.getAccount());
        ImcFrame frame = new ImcFrame(String.format("put /?linestate=offling imc/1.0"), String.format("%s@linestate.sys", sender.toDescriptor()));
        kafkaTemplate.send(eventTopicName, frame.toText());
    }
}
