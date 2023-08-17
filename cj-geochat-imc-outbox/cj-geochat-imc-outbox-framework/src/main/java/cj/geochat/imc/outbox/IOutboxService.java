package cj.geochat.imc.outbox;

import cj.geochat.imc.common.ImcFrame;

public interface IOutboxService {
    void outbox(ImcFrame frame);
}
