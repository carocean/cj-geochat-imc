package cj.geochat.imc.comet.distributor;

import cj.geochat.imc.comet.IChannelDistributor;
import cj.geochat.imc.comet.IOfflineFrameWriter;
import cj.geochat.imc.comet.annotation.CjChannelType;
import cj.geochat.imc.comet.remote.MemberRemote;
import cj.geochat.imc.comet.remote.NodeSelectorRemote;
import cj.geochat.imc.comet.ws.ICometEndpointContainer;
import cj.geochat.imc.common.ImcFrame;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

//地理群
@CjChannelType("geogroup")
@Service
public class GeogroupChannelDistributor implements IChannelDistributor {
    @Resource
    ICometEndpointContainer cometEndpointContainer;
    @Resource
    NodeSelectorRemote nodeSelectorRemote;
    @Resource
    IOfflineFrameWriter offlineFrameWriter;
    @Resource
    MemberRemote memberRemote;

    @Override
    public void distribute(ImcFrame frame) {

    }
}
