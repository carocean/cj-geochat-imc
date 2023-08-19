package cj.geochat.imc.comet.distributor;

import cj.geochat.imc.comet.IChannelDistributor;
import cj.geochat.imc.comet.annotation.CjChannelType;
import cj.geochat.imc.comet.ws.ICometEndpoint;
import cj.geochat.imc.comet.ws.ICometEndpointContainer;
import cj.geochat.imc.common.ImcFrame;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

//所有会话
@CjChannelType("session")
@Service
public class SessionChannelDistributor implements IChannelDistributor {
    @Resource
    ICometEndpointContainer cometEndpointContainer;

    @Override
    public void distribute(ImcFrame frame) {
        String channel = frame.channel().getChannel();
        if ("*".equals(channel)) {
            List<ICometEndpoint> all = cometEndpointContainer.allCometEndpoint();
            for (ICometEndpoint cometEndpoint : all) {
                String user = cometEndpoint.principal().getName();
                if (frame.inRejects(user)) {
                    continue;
                }
                if (!frame.inRecipients(user)) {
                    continue;
                }
                cometEndpoint.send(frame.copy());
            }
            return;
        }
        ICometEndpoint cometEndpoint = cometEndpointContainer.findCometEndpointBySessionId(channel);
        if (cometEndpoint == null) {
            return;
        }
        String user = cometEndpoint.principal().getName();
        if (frame.inRejects(user)) {
            return;
        }
        if (!frame.inRecipients(user)) {
            return;
        }
        cometEndpoint.send(frame.copy());
    }
}
