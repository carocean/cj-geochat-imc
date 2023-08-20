package cj.geochat.imc.comet.service;

import cj.geochat.imc.comet.IOnlineTableManager;
import cj.geochat.imc.comet.ws.ICometEndpoint;
import com.google.gson.Gson;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Set;

@Service
public class DefaultOnlineTableManager implements IOnlineTableManager, InitializingBean {
    static String _KEY_USERS = "imc:online:users";
    static String _KEY_NODES = "imc:online:nodes";
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Value("${spring.application.name}")
    String nodeName;

    @Override
    public void afterPropertiesSet() throws Exception {
        String nodeKey=String.format("%s:%s", _KEY_NODES, nodeName);
        Set<String> users = redisTemplate.opsForSet().members(nodeKey);
        for (String user : users) {
            redisTemplate.opsForHash().delete(_KEY_USERS, user);
        }
       redisTemplate.delete(nodeKey);
    }

    @Override
    public void online(ICometEndpoint cometEndpoint) {
        var principal = cometEndpoint.principal();
        var map = new HashMap<>();
        map.put("app-id", principal.getAppid());
        map.put("account", principal.getAccount());
        map.put("on-node", nodeName);
        map.put("time", System.currentTimeMillis());
        redisTemplate.opsForHash().put(_KEY_USERS, principal.getName(), new Gson().toJson(map));
        redisTemplate.opsForSet().add(String.format("%s:%s", _KEY_NODES, nodeName), principal.getName());
    }

    @Override
    public void offline(ICometEndpoint cometEndpoint) {
        var principal = cometEndpoint.principal();
        redisTemplate.opsForHash().delete(_KEY_USERS, principal.getName());
        redisTemplate.opsForSet().remove(String.format("%s:%s", _KEY_NODES, nodeName), principal.getName());
    }

    @Override
    public boolean isOnline(String user) {
        String json = (String) redisTemplate.opsForHash().get(_KEY_USERS, user);
        return StringUtils.hasLength(json);
    }
}
