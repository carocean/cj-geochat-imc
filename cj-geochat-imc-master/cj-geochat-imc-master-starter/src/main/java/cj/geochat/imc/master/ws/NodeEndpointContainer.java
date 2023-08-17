package cj.geochat.imc.master.ws;

import cj.geochat.imc.master.INodeEndpoint;
import cj.geochat.imc.master.INodeEndpointContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NodeEndpointContainer implements INodeEndpointContainer {
    List<INodeEndpoint> nodeEndpoints = new CopyOnWriteArrayList<>();
    List<String> nodeNames = new CopyOnWriteArrayList<>();
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void add(INodeEndpoint nodeEndpoint) {
        nodeEndpoints.add(nodeEndpoint);
        nodeNames.add(nodeEndpoint.nodeName());
        String key = "imc:master:nodes";
        redisTemplate.opsForHash().put(key, nodeEndpoint.nodeName(), "true");
    }

    @Override
    public void remove(INodeEndpoint nodeEndpoint) {
        String key = "imc:master:nodes";
        redisTemplate.opsForHash().delete(key, nodeEndpoint.nodeName());
        nodeEndpoints.remove(nodeEndpoint);
        nodeNames.remove(nodeEndpoint.nodeName());
    }

    public String selectNodeName(int factor) {
        if (nodeNames.isEmpty()) {
            return null;
        }
        return nodeNames.get(factor % nodeNames.size());
    }
}
