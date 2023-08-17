package cj.geochat.imc.comet.ws;

public interface ICometServiceFactory {
    ICometEndpointContainer endpointContainer();

    IOnmessageService messageService();

    ILineEventService lineEventService();

    ICometNodeRegister cometNodeRegister();
}
