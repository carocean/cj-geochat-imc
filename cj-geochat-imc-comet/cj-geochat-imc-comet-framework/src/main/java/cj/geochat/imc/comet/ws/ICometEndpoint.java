package cj.geochat.imc.comet.ws;

import cj.geochat.ability.oauth.app.principal.DefaultAppPrincipal;
import cj.geochat.imc.common.ImcFrame;

public interface ICometEndpoint {
    String sessionId();
    DefaultAppPrincipal principal();
    void send(ImcFrame frame) ;
}
