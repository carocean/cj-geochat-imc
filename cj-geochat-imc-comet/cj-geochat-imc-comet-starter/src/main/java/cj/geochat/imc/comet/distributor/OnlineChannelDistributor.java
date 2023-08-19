package cj.geochat.imc.comet.distributor;

import cj.geochat.imc.comet.IChannelDistributor;
import cj.geochat.imc.comet.annotation.CjChannelType;
import cj.geochat.imc.comet.ws.ICometEndpoint;
import cj.geochat.imc.comet.ws.ICometEndpointContainer;
import cj.geochat.imc.common.ImcFrame;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

//所有在线用户
@CjChannelType("online")
@Service
public class OnlineChannelDistributor implements IChannelDistributor {
    @Resource
    ICometEndpointContainer cometEndpointContainer;

    @Override
    public void distribute(ImcFrame frame) {
        String channel = frame.channel().getChannel();
        if ("*".equals(channel)) {
            String users[] = cometEndpointContainer.enumOnlineUser();
            for (String user : users) {
                if (frame.inRejects(user)) {
                    continue;
                }
                if (!frame.inRecipients(user)) {
                    continue;
                }
                List<ICometEndpoint> cometEndpoints = cometEndpointContainer.findCometEndpointByUser(user);
                for (ICometEndpoint cometEndpoint : cometEndpoints) {
                    cometEndpoint.send(frame.copy());
                }
            }
            return;
        }
        if (frame.inRejects(channel)) {
            return;
        }
        if (!frame.inRecipients(channel)) {
            return;
        }
        List<ICometEndpoint> cometEndpoints = cometEndpointContainer.findCometEndpointByUser(channel);
        for (ICometEndpoint cometEndpoint : cometEndpoints) {
            cometEndpoint.send(frame.copy());
        }
    }
}
