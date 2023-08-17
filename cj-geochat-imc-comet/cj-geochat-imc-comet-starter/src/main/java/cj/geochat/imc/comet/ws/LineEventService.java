package cj.geochat.imc.comet.ws;

import cj.geochat.imc.comet.IOnlineTableManager;
import cj.geochat.imc.comet.IPrincipalChannelService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
只维护channel与user关系，成员不是确定user的不维护，比如channel代表app时其成员不确定。
上线：
- 维护用户在线列表
- 加载channel及成员到redis
下线：
- 维护用户在线列表
- 从channel成员中移除
 */
@Service
public class LineEventService implements ILineEventService {
    @Autowired
    ICometEndpointContainer cometEndpointContainer;

    @Resource
    IOnlineTableManager onlineTableManager;
    @Resource
    IPrincipalChannelService principalChannelService;

    @Override
    public void online(String sessionId) {
        ICometEndpoint cometEndpoint = cometEndpointContainer.findCometEndpointBySessionId(sessionId);
        onlineTableManager.online(cometEndpoint);
        principalChannelService.online(cometEndpoint.principal());
    }

    @Override
    public void offline(String sessionId) {
        ICometEndpoint cometEndpoint = cometEndpointContainer.findCometEndpointBySessionId(sessionId);
        onlineTableManager.offline(cometEndpoint);
        principalChannelService.offline(cometEndpoint.principal());
    }

}
