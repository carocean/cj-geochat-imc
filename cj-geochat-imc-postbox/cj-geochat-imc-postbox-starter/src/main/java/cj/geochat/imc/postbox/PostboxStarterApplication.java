package cj.geochat.imc.postbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.imc.postbox"})
public class PostboxStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostboxStarterApplication.class, args);
    }

}
