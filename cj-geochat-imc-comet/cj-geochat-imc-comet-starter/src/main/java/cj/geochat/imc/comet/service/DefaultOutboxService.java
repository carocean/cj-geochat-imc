package cj.geochat.imc.comet.service;

import cj.geochat.imc.comet.IOfflineFrameWriter;
import cj.geochat.imc.comet.IOutboxService;
import cj.geochat.imc.comet.remote.MemberRemote;
import cj.geochat.imc.comet.remote.NodeSelectorRemote;
import cj.geochat.imc.comet.ws.ICometEndpointContainer;
import cj.geochat.imc.common.ImcChannel;
import cj.geochat.imc.common.ImcFrame;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DefaultOutboxService implements IOutboxService {
    @Resource
    ICometEndpointContainer cometEndpointContainer;
    @Resource
    NodeSelectorRemote nodeSelectorRemote;
    @Resource
    IOfflineFrameWriter offlineFrameWriter;
    @Resource
    MemberRemote memberRemote;

    @Override
    public void outbox(ImcFrame frame) {
        ImcChannel channel = frame.channel();
        String[] rejects = frame.rejectsToList();
        String[] recipients = frame.recipientsToList();
        //根据channel查成员，关系运算。
        switch (channel.getType()) {
            case "app":
                //一个用户可能有多个会话，一个应用有多个用户和多个会话
//                ICometEndpoint cometEndpoint = cometEndpointContainer.findCometEndpointByAppId(channel.getChannel());
//                if (cometEndpoint != null) {
//                    cometEndpoint.send(frame);
//                    break;
//                }

                break;
            case "gp"://group
                break;
            case "ct"://chat
                break;
            case "gc"://地微对话
                break;
            case "gb"://地微广播
                break;
            case "ar"://地微实现
                break;
            case "gpay"://地微支付
                break;
            case "ao"://所有在线会话
                break;
            default:
                //不支持
                break;
        }
    }
}
