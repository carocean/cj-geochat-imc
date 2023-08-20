package cj.geochat.imc.comet.service;

import cj.geochat.ability.oauth.app.principal.DefaultAppPrincipal;
import cj.geochat.imc.comet.IReadOfflineService;
import cj.geochat.imc.comet.remote.OfflineFrameApi;
import cj.geochat.imc.comet.ws.ICometEndpoint;
import cj.geochat.imc.comet.ws.ICometEndpointContainer;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.postbox.entity.OfflineDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadOfflineService implements IReadOfflineService {
    @Autowired
    OfflineFrameApi offlineFrameApi;
    @Autowired
    ICometEndpointContainer cometEndpointContainer;

    @Override
    public void onread(DefaultAppPrincipal principal) {
        List<ICometEndpoint> cometEndpoints = cometEndpointContainer.findCometEndpointByUser(principal.getName());
        int limit = 200;
        int offset = 0;
        List<String> frameRaws = null;
        while (!(frameRaws = offlineFrameApi.readAndDelete(principal.getName(), limit, offset)).isEmpty()) {
            for (String raw : frameRaws) {
                ImcFrame frame = ImcFrame.fromText(raw);
                for (ICometEndpoint cometEndpoint : cometEndpoints) {
                    cometEndpoint.send(frame);
                }
            }
            offset += frameRaws.size();
        }
    }
}
