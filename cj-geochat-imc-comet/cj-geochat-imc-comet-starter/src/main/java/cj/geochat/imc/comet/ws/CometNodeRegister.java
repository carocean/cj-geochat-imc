package cj.geochat.imc.comet.ws;

import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CometNodeRegister implements ICometNodeRegister, ApplicationListener {
    @Value("${spring.application.name}")
    String nodeName;
    @Value("${comet.master.node}")
    String masterUrl;
    WebSocketClient client;

    ScheduledExecutorService executorService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent ready) {
            onApplicationReady();
        }
        if (event instanceof ContextClosedEvent closed) {
            onApplicationClosed();
        }
    }

    @SneakyThrows
    private void onApplicationReady() {
        executorService = Executors.newScheduledThreadPool(1);
        String ws = String.format("%s?node-name=%s", masterUrl, nodeName);
        client = new NodeClient(new URI(ws), this::onclose);
        try {
            client.connect();
        } catch (Exception e) {

        }
    }

    private void onclose(int times) {
        int seconds = times * 2;
        if (seconds < 30) {
            executorService.schedule(new DefaultRunnable(), seconds, TimeUnit.SECONDS);
        } else {
            seconds = 30;
            executorService.schedule(new DefaultRunnable(), seconds, TimeUnit.SECONDS);
        }
    }

    private void onApplicationClosed() {
        if (client != null && !client.isOpen()) {
            client.close();
        }
    }

    class DefaultRunnable implements Runnable {

        @Override
        public void run() {
            client.reconnect();
        }
    }
}
