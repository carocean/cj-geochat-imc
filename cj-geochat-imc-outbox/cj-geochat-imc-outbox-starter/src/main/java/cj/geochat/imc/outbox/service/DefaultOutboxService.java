package cj.geochat.imc.outbox.service;

import cj.geochat.imc.common.ImcFrame;
import cj.geochat.imc.outbox.IOutboxFactory;
import cj.geochat.imc.outbox.IOutboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultOutboxService implements IOutboxService {
    @Autowired
    IOutboxFactory outboxFactory;

//    @Transactional
    @Override
    public void outbox(ImcFrame frame) {
        outboxFactory.broadcast(frame);
    }
}
