package cj.geochat.imc.comet.service;

import cj.geochat.ability.oauth.app.principal.DefaultAppPrincipal;
import cj.geochat.imc.comet.IChannelMemberService;
import cj.geochat.imc.comet.IPrincipalChannelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

//关系在es库中，本服务标识当事人成员上线状态
@Service
public class DefaultPrincipalChannelService implements IPrincipalChannelService {
    @Resource
    IChannelMemberService channelMemberService;

    @Override
    public void online(DefaultAppPrincipal principal) {
//        channelMemberService.updateMemberLineState(principal.getName(),true);
    }

    @Override
    public void offline(DefaultAppPrincipal principal) {
//        channelMemberService.updateMemberLineState(principal.getName(),false);
    }
}
