package cj.geochat.imc.comet.ws;

import cj.geochat.ability.oauth.app.principal.DefaultAppPrincipal;
import cj.geochat.imc.comet.config.OpenWebsocketConfig;
import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.common.ImcSender;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ServerEndpoint 该注解可以将类定义成一个WebSocket服务器端，
 * @OnOpen 表示有浏览器链接过来的时候被调用
 * @OnClose 表示浏览器发出关闭请求的时候被调用
 * @OnMessage 表示浏览器发消息的时候被调用
 * @OnError 表示报错了
 */
@ServerEndpoint(value = "/ws/v1/comet", configurator = OpenWebsocketConfig.class)
@Component
@Slf4j
public class CometEndpoint implements ApplicationContextAware, ICometEndpoint {
    private static ICometServiceFactory cometServiceFactory;
    private Session session;

    @Override
    public String sessionId() {
        return session.getId();
    }

    public DefaultAppPrincipal principal() {
        return (DefaultAppPrincipal) ((Authentication) session.getUserPrincipal()).getPrincipal();
    }

    public void send(ImcFrame frame) {
        try {
            this.session.getBasicRemote().sendText(frame.toText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (cometServiceFactory == null) {
            cometServiceFactory = applicationContext.getBean(ICometServiceFactory.class);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Authentication authentication = (Authentication) session.getUserPrincipal();
        var ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(authentication);
        SecurityContextHolder.setContext(ctx);
        cometServiceFactory.endpointContainer().onopen(this);
        cometServiceFactory.lineEventService().online(sessionId());
    }

    @OnClose
    public void onClose() {
        SecurityContextHolder.clearContext();
        try {
            cometServiceFactory.lineEventService().offline(sessionId());
            cometServiceFactory.endpointContainer().onclose(this);
        } catch (Throwable throwable) {
            log.error("onClose", throwable);
            onError(throwable);
        }
    }

    @OnMessage(maxMessageSize = 10485760)
    public void onMessage(String message) {
        try {
            ImcFrame frame = ImcFrame.fromText(message);
            DefaultAppPrincipal principal = principal();
            ImcSender sender = ImcSender.toSender(principal.getAppid(), principal.getName(), principal.getAccount());
            frame.sender(sender);
            cometServiceFactory.messageService().onmessage(sessionId(), frame);
        } catch (Throwable throwable) {
            log.error("onMessage", throwable);
            onError(throwable);
        }
    }

    @OnError
    public void onError(Throwable throwable) {
        try {
            cometServiceFactory.messageService().onerror(sessionId(), throwable);
        } catch (Throwable e) {
            log.error("onError", e);
        }
    }


}

