package cj.geochat.imc.comet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.imc.comet"})
public class CometStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CometStarterApplication.class, args);
    }

}
