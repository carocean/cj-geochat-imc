package cj.geochat.imc.comet.distributor;

import cj.geochat.imc.comet.IChannelDistributor;
import cj.geochat.imc.comet.IOfflineFrameWriter;
import cj.geochat.imc.comet.annotation.CjChannelType;
import cj.geochat.imc.comet.remote.MemberRemote;
import cj.geochat.imc.comet.remote.NodeSelectorRemote;
import cj.geochat.imc.comet.ws.ICometEndpoint;
import cj.geochat.imc.comet.ws.ICometEndpointContainer;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.soc.entity.Member;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

//群组
@CjChannelType("group")
@Service
public class GroupChannelDistributor implements IChannelDistributor {
    @Resource
    ICometEndpointContainer cometEndpointContainer;
    @Resource
    NodeSelectorRemote nodeSelectorRemote;
    @Resource
    IOfflineFrameWriter offlineFrameWriter;
    @Resource
    MemberRemote memberRemote;
    @Value("${spring.application.name}")
    String nodeName;

    @Override
    public void distribute(ImcFrame frame) {
        String group = frame.channel().getChannel();
        int limit = 200;
        int offset = 0;
        int readed = 0;
        while ((readed = _distribute(group, limit, offset, frame)) > 0) {
            offset += readed;
        }
    }

    private int _distribute(String group, int limit, int offset, ImcFrame frame) {
        List<Member> members = memberRemote.listMemberByChannel(group, limit, offset);
        for (Member member : members) {
            String user = member.getMember();
            if (frame.inRejects(user) || !frame.inRecipients(user)) {
                continue;
            }
            List<ICometEndpoint> cometEndpoints = cometEndpointContainer.findCometEndpointByUser(user);
            if (!cometEndpoints.isEmpty()) {
                for (ICometEndpoint cometEndpoint : cometEndpoints) {
                    cometEndpoint.send(frame.copy());
                }
            } else {//用户不在此comet节点上，或许根本不在线
                String node = nodeSelectorRemote.selectNode(user);
                if (nodeName.equals(node)) {
                    //如果是当前节点写离线消息则写入
                    offlineFrameWriter.write(user, frame.copy());
                }
            }

        }
        return members.size();
    }
}
