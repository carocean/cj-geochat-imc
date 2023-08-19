package cj.geochat.imc.comet.ws;

import cj.geochat.ability.oauth.app.principal.DefaultAppAuthentication;
import cj.geochat.imc.common.ImcChannel;
import cj.geochat.imc.common.ImcFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
@Slf4j
public class OnmessageService implements IOnmessageService {
    @Autowired
    ICometEndpointContainer cometEndpointContainer;
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Value("${comet.event-topic.name}")
    String eventTopicName;

    @Override
    public void onmessage(String sessionId, ImcFrame frame) {
        //主要用于位置更新
        ImcChannel channel = frame.channel();
        if (!"geo".equals(channel.getType()) && !"location".equalsIgnoreCase(channel.getChannel())) {
            log.warn("收到的客户端消息非位置更新消息，仅接收位置更新消息");
            return;
        }
        String longitude = frame.parameter("longitude");
        String latitude = frame.parameter("latitude");
        if (!StringUtils.hasLength(longitude) || !StringUtils.hasLength(latitude)) {
            log.warn("经纬度或为空");
            return;
        }
        kafkaTemplate.send(eventTopicName, sessionId, frame.toText());
    }

    @Override
    public void onerror(String sessionId, Throwable throwable) {
        ICometEndpoint endpoint = cometEndpointContainer.findCometEndpointBySessionId(sessionId);
        if (endpoint == null) {
            return;
        }
        ImcFrame frame = new ImcFrame("post /error imc/1.0", "alert.sys@platform.sys");
        frame.body().append(throwable.getMessage());
        endpoint.send(frame);
    }
}
