package cj.geochat.imc.comet.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CometServiceFactory implements ICometServiceFactory {
    @Autowired
    ICometEndpointContainer endpointContainer;
    @Autowired
    IOnmessageService messageService;
    @Autowired
    ILineEventService lineEventService;
    @Autowired
    ICometNodeRegister cometNodeRegister;


    @Override
    public ICometEndpointContainer endpointContainer() {
        return this.endpointContainer;
    }

    @Override
    public IOnmessageService messageService() {
        return this.messageService;
    }

    @Override
    public ILineEventService lineEventService() {
        return this.lineEventService;
    }

    @Override
    public ICometNodeRegister cometNodeRegister() {
        return cometNodeRegister;
    }
}
