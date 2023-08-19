package cj.geochat.imc.comet;

import cj.geochat.imc.comet.ws.ICometEndpoint;

public interface IOnlineTableManager {
    void online(ICometEndpoint cometEndpoint);

    void offline(ICometEndpoint cometEndpoint);

    boolean isOnline(String user);

}
