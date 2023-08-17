package cj.geochat.imc.outbox;

import cj.geochat.imc.common.Customizer;
import cj.geochat.imc.common.ImcFrame;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.List;

public interface IOutboxFactory {
    void init(Customizer<List<NewTopic>> customizer);
    void unicast(ImcFrame copy);

    void broadcast(ImcFrame copy);

}
