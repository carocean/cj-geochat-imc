package cj.geochat.imc.inbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.imc.inbox"})
public class InboxStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(InboxStarterApplication.class, args);
    }

}
