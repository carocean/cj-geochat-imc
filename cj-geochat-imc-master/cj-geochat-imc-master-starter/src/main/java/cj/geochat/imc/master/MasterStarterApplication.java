package cj.geochat.imc.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.imc.master"})
public class MasterStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MasterStarterApplication.class, args);
    }

}
