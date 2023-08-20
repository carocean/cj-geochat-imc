package cj.geochat.imc.postbox.service;

import cj.geochat.imc.common.ImcChannel;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.common.ImcSender;
import cj.geochat.imc.postbox.ILinestateEventService;
import cj.geochat.imc.postbox.remote.MemberRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinestateEventService implements ILinestateEventService {
    @Autowired
    MemberRemote memberRemote;

    @Override
    public void update(ImcFrame frame) {
        ImcSender sender = frame.sender();
        String user = sender.tryGetUser();
        String linestate = frame.parameter("linestate");
        if ("online".equals(linestate)) {
            memberRemote.updateOnline(user, true);
        } else {
            memberRemote.updateOnline(user, false);
        }
    }
}
