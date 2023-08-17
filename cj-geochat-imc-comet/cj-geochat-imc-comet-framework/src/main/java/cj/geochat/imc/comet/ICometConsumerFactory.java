package cj.geochat.imc.comet;

import cj.geochat.imc.common.Customizer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.List;

@FunctionalInterface
public interface ICometConsumerFactory {
    void init(Customizer<List<ConcurrentMessageListenerContainer<String, String>>> customizer);
}
