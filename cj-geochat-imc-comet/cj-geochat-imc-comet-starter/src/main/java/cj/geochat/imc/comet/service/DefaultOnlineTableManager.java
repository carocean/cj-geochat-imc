package cj.geochat.imc.comet.service;

import cj.geochat.imc.comet.IOnlineTableManager;
import cj.geochat.imc.comet.ws.ICometEndpoint;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DefaultOnlineTableManager implements IOnlineTableManager {
    @Autowired
    RedisTemplate<String, String> redisTemplate;
@Value("${spring.application.name}")
    String nodeName;
    @Override
    public void online(ICometEndpoint cometEndpoint) {
        var principal = cometEndpoint.principal();
        var map = new HashMap<>();
        map.put("app-id", principal.getAppid());
        map.put("account", principal.getAccount());
        map.put("on-node", nodeName);
        map.put("time", System.currentTimeMillis());
        redisTemplate.opsForHash().put("imc:online:users", principal.getName(), new Gson().toJson(map));
    }

    @Override
    public void offline(ICometEndpoint cometEndpoint) {
        var principal = cometEndpoint.principal();
        redisTemplate.opsForHash().delete("imc:online:users",principal.getName());
    }
}
