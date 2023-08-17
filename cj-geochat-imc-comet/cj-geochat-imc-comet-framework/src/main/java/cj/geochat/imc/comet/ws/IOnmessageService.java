package cj.geochat.imc.comet.ws;

import cj.geochat.imc.common.ImcFrame;

import java.io.IOException;

public interface IOnmessageService {
    void onmessage(String sessionId, ImcFrame frame);

    void onerror(String sessionId, Throwable throwable);

}
