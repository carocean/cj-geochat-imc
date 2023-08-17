package cj.geochat.imc.comet;

import cj.geochat.ability.oauth.app.principal.DefaultAppPrincipal;

public interface IPrincipalChannelService {
    void online(DefaultAppPrincipal principal);

    void offline(DefaultAppPrincipal principal);

}
