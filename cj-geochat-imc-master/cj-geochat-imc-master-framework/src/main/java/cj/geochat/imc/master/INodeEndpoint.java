package cj.geochat.imc.master;

import cj.geochat.imc.common.ImcFrame;

public interface INodeEndpoint {
    String sessionId();
    void send(ImcFrame frame) throws Exception;

    String nodeName();

}
