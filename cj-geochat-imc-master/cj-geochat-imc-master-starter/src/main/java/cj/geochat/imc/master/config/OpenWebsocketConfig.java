package cj.geochat.imc.master.config;

import cj.geochat.ability.util.GeochatRuntimeException;
import cj.geochat.ability.websocket.annotation.EnableCjWebsocket;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;

@EnableCjWebsocket
@Configuration
//ServerEndpointConfig.Configurator 要通过@ServerEndpoint注解指定才生效，它只认类型而不是服务
public class OpenWebsocketConfig extends ServerEndpointConfig.Configurator {

    /**
     * 建立握手时，连接前的操作
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 可建立http请求和ws的属性的传递，通过sec.userProperties，在ws中可以通过 sec.getUserProperties()获取
        //在此可拒绝连接
        String nodeName=request.getParameterMap().get("node-name").stream().findFirst().orElse(null);
        if (!StringUtils.hasLength(nodeName)) {
            throw new GeochatRuntimeException("500","Parameter node-name is Required");
        }
        sec.getUserProperties().put("node-name", nodeName);
        super.modifyHandshake(sec, request, response);
    }

    /**
     * 初始化端点对象,也就是被@ServerEndpoint所标注的对象
     */
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return super.getEndpointInstance(clazz);
    }
}
