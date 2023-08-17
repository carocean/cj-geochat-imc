package cj.geochat.imc.comet.ws;

import cj.geochat.imc.common.ImcFrame;

import java.io.IOException;
import java.util.List;

public interface ICometEndpointContainer {
    void onopen(ICometEndpoint cometEndpoint);

    void onclose(ICometEndpoint cometEndpoint);

    ICometEndpoint findCometEndpointBySessionId(String sid);

    List<ICometEndpoint> findCometEndpointByUser(String user);

    List<ICometEndpoint> findCometEndpointByAccount(String account);

    List<ICometEndpoint> findCometEndpointByAppId(String appid);

}
