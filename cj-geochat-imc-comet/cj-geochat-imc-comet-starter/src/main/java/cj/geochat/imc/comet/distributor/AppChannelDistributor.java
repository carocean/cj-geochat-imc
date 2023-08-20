package cj.geochat.imc.comet.distributor;

import cj.geochat.imc.comet.IChannelDistributor;
import cj.geochat.imc.comet.annotation.CjChannelType;
import cj.geochat.imc.comet.ws.ICometEndpoint;
import cj.geochat.imc.comet.ws.ICometEndpointContainer;
import cj.geochat.imc.common.ImcFrame;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

//所指定的app下的所有在线用户
@CjChannelType("app")
@Service
public class AppChannelDistributor implements IChannelDistributor {
    @Resource
    ICometEndpointContainer cometEndpointContainer;

    @Override
    public void distribute(ImcFrame frame) {
        String appId = frame.channel().getChannel();
        List<ICometEndpoint> cometEndpoints = cometEndpointContainer.findCometEndpointByAppId(appId);
        for (ICometEndpoint cometEndpoint : cometEndpoints) {
            String user = cometEndpoint.principal().getName();
            if (frame.inRejects(user)) {
                continue;
            }
            if (!frame.inRecipients(user)) {
               continue;
            }
            cometEndpoint.send(frame.copy());
        }
    }
}
