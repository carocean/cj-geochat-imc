package cj.geochat.imc.comet;

import cj.geochat.imc.common.ImcFrame;

public interface IOutboxService {
    void outbox(ImcFrame frame);
}
