package cj.geochat.imc.outbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.imc.outbox"})
public class OutboxStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutboxStarterApplication.class, args);
    }

}
