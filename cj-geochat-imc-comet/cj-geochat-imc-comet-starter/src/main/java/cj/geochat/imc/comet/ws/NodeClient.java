package cj.geochat.imc.comet.ws;

import cj.geochat.imc.common.Customizer;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

@Slf4j
public class NodeClient extends WebSocketClient {
    int close_times = 0;
    Customizer<Integer> customizer;

    public NodeClient(URI serverUri, Customizer<Integer> customizer) {
        super(serverUri);
        this.customizer = customizer;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("已注册到主节点");
    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        customizer.customize(close_times++);
        log.info("comet从主节点移除。原因：" + i + " " + s);
    }

    @Override
    public void onError(Exception e) {

    }
}
