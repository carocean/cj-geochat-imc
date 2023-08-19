package cj.geochat.imc.comet.ws;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class CometEndpointContainer implements ICometEndpointContainer {
    private final CopyOnWriteArraySet<ICometEndpoint> cometEndpoints = new CopyOnWriteArraySet<>();
    private Map<String, ICometEndpoint> indexCometEndpointOfSid = new ConcurrentHashMap<>();
    private Map<String, List<ICometEndpoint>> indexCometEndpointsOfUser = new ConcurrentHashMap<>();
    private Map<String, List<ICometEndpoint>> indexCometEndpointsOfAccount = new ConcurrentHashMap<>();
    private Map<String, List<ICometEndpoint>> indexCometEndpointsOfAppId = new ConcurrentHashMap<>();
    @Override
    public String[] enumOnlineApp(){
        return indexCometEndpointsOfAppId.keySet().toArray(new String[0]);
    }
    @Override
    public String[] enumOnlineUser() {
        return indexCometEndpointsOfUser.keySet().toArray(new String[0]);
    }

    @Override
    public List<ICometEndpoint> allCometEndpoint() {
        return cometEndpoints.stream().toList();
    }

    @Override
    public void onopen(ICometEndpoint cometEndpoint) {
        cometEndpoints.add(cometEndpoint);
        var principal = cometEndpoint.principal();
        indexCometEndpointOfSid.put(cometEndpoint.sessionId(), cometEndpoint);
        List<ICometEndpoint> cometEndpoints = indexCometEndpointsOfAppId.get(principal.getAppid());
        if (cometEndpoints == null) {
            cometEndpoints = new ArrayList<>();
            indexCometEndpointsOfAppId.put(principal.getAppid(), cometEndpoints);
        }
        cometEndpoints.add(cometEndpoint);
        cometEndpoints = indexCometEndpointsOfAccount.get(principal.getAccount());
        if (cometEndpoints == null) {
            cometEndpoints = new ArrayList<>();
            indexCometEndpointsOfAccount.put(principal.getAccount(), cometEndpoints);
        }
        cometEndpoints.add(cometEndpoint);
        cometEndpoints = indexCometEndpointsOfUser.get(principal.getName());
        if (cometEndpoints == null) {
            cometEndpoints = new ArrayList<>();
            indexCometEndpointsOfUser.put(principal.getName(), cometEndpoints);
        }
        cometEndpoints.add(cometEndpoint);
    }


    @Override
    public void onclose(ICometEndpoint cometEndpoint) {
        cometEndpoints.remove(cometEndpoint);
        var principal = cometEndpoint.principal();
        indexCometEndpointOfSid.remove(cometEndpoint.sessionId());
        var cometEndpoints = indexCometEndpointsOfAppId.get(principal.getAppid());
        cometEndpoints.remove(cometEndpoint);
        cometEndpoints = indexCometEndpointsOfAccount.get(principal.getAccount());
        cometEndpoints.remove(cometEndpoint);
        cometEndpoints = indexCometEndpointsOfUser.get(principal.getName());
        cometEndpoints.remove(cometEndpoint);
    }


    @Override
    public ICometEndpoint findCometEndpointBySessionId(String sid) {
        return indexCometEndpointOfSid.get(sid);
    }

    @Override
    public List<ICometEndpoint> findCometEndpointByUser(String user) {
        var list= indexCometEndpointsOfUser.get(user);
        return list == null ? new ArrayList<>() : list;
    }

    @Override
    public List<ICometEndpoint> findCometEndpointByAccount(String account) {
        var list= indexCometEndpointsOfAccount.get(account);
        return list == null ? new ArrayList<>() : list;
    }

    @Override
    public List<ICometEndpoint> findCometEndpointByAppId(String appid) {
        var list= indexCometEndpointsOfAppId.get(appid);
        return list == null ? new ArrayList<>() : list;
    }
}
