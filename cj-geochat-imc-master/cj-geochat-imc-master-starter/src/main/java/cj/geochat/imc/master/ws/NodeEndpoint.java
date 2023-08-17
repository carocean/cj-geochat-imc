package cj.geochat.imc.master.ws;

import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.master.INodeEndpoint;
import cj.geochat.imc.master.INodeEndpointContainer;
import cj.geochat.imc.master.config.OpenWebsocketConfig;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ServerEndpoint 该注解可以将类定义成一个WebSocket服务器端，
 * @OnOpen 表示有浏览器链接过来的时候被调用
 * @OnClose 表示浏览器发出关闭请求的时候被调用
 * @OnMessage 表示浏览器发消息的时候被调用
 * @OnError 表示报错了
 */
@ServerEndpoint(value = "/ws/v1/master", configurator = OpenWebsocketConfig.class)
@Component
@Slf4j
public class NodeEndpoint implements ApplicationContextAware, INodeEndpoint {
    private static INodeEndpointContainer nodeEndpointContainer;
    private Session session;

    @Override
    public String sessionId() {
        return session.getId();
    }

    @Override
    public String nodeName() {
        return (String) session.getUserProperties().get("node-name");
    }

    public void send(ImcFrame frame) throws Exception {
        this.session.getBasicRemote().sendText(frame.toText());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (nodeEndpointContainer == null) {
            nodeEndpointContainer = applicationContext.getBean(INodeEndpointContainer.class);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        nodeEndpointContainer.add(this);
    }

    @OnClose
    public void onClose() {
        try {
            nodeEndpointContainer.remove(this);
        } catch (Throwable throwable) {
            log.error("onClose", throwable);
            onError(throwable);
        }
    }

    @OnMessage
    public void onMessage(String message) {

    }

    @OnError
    public void onError(Throwable throwable) {
        log.error("onError", throwable);
    }


}

