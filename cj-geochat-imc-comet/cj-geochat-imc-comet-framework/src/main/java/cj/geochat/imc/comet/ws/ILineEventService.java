package cj.geochat.imc.comet.ws;

public interface ILineEventService {
    void offline(String sessionId);

    void online(String sessionId);

}
