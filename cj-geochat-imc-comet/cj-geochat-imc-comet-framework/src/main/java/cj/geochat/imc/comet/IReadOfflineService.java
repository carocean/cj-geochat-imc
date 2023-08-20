package cj.geochat.imc.comet;

import cj.geochat.ability.oauth.app.principal.DefaultAppPrincipal;

public interface IReadOfflineService {
    void onread(DefaultAppPrincipal principal);

}
