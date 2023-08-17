package cj.geochat.imc.comet.ws;

import cj.geochat.imc.common.ImcFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OnmessageService implements IOnmessageService {
    @Autowired
    ICometEndpointContainer cometEndpointContainer;

    @Override
    public void onmessage(String sessionId, ImcFrame frame) {
        System.out.println(frame);
    }

    @Override
    public void onerror(String sessionId, Throwable throwable) {
        ICometEndpoint endpoint = cometEndpointContainer.findCometEndpointBySessionId(sessionId);
        if (endpoint == null) {
            return;
        }
        ImcFrame frame = new ImcFrame("post /error imc/1.0","alert.sys@platform.sys");
        frame.body().append(throwable.getMessage());
        endpoint.send(frame);
    }
}
